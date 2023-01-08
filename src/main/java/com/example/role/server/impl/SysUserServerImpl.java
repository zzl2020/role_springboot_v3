package com.example.role.server.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.crypto.digest.MD5;
import com.example.role.commons.CommonConst;
import com.example.role.commons.jwt.TokenUtil;
import com.example.role.commons.util.CommonUtil;
import com.example.role.commons.util.ImgUtils;
import com.example.role.commons.util.R;
import com.example.role.commons.util.RConst;
import com.example.role.commons.util.exception.CustomException;
import com.example.role.commons.util.redis.RedisCacheClient;
import com.example.role.entity.SysUser;
import com.example.role.entity.SysUserRole;
import com.example.role.entity.dto.LoginDto;
import com.example.role.entity.dto.RegisterDto;
import com.example.role.entity.dto.UpdateUserDto;
import com.example.role.entity.dto.UserDto;
import com.example.role.mapper.SysUserMapper;
import com.example.role.mapper.SysUserRoleMapper;
import com.example.role.server.CommonServer;
import com.example.role.server.SmsServer;
import com.example.role.server.SysUserServer;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SysUserServerImpl implements SysUserServer {
    @Value("${upload.filePath}")
    private String IMG_PATH;
    @Value("${print.code}")
    private boolean PRINT_CODE;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private CommonServer commonServer;
    @Resource
    private RedisCacheClient redisCacheClient;
    @Resource
    private SmsServer smsServer;
    public int insert(SysUser sysUser) {
        return sysUserMapper.insert(sysUser);
    }

    public SysUser findById(Integer id) {
        return sysUserMapper.findById(id);
    }

    public List<SysUser> findAll() {
        return sysUserMapper.findAll();
    }
    @Transactional
    public R update(UpdateUserDto updateUserDto) {
        SysUser user = sysUserMapper.getUserByPhone(updateUserDto.getPhone());
        if (ObjectUtils.isEmpty(user)) {
            throw new CustomException(RConst.ERROR_CODE, CommonConst.UPDATE_INFO_ERROR_MSG);
        }
        BeanUtils.copyProperties(updateUserDto,user);
        int num = sysUserMapper.update(user);
        if (num==0) throw new  CustomException(RConst.ERROR_CODE,"个人信息修改失败");
        return R.ok();
    }

    public int delete(Integer id) {
        return sysUserMapper.delete(id);
    }
    @Transactional
    @Override
    public R userAddRole(UserDto userDto) {
        Integer uid = userDto.getUid();
        Assert.notNull(uid,"uid is null");
        List<Integer> rids = userDto.getRids();
        Assert.notNull(userDto, "userDto is null");
        if (rids.size() == 0) {
            throw new CustomException();
        }
        SysUserRole su = new SysUserRole();
        su.setUserId(uid);
        rids.forEach(t -> {
            Assert.notNull(t," rid is null");
            su.setRoleId(t);
            sysUserRoleMapper.insert(su);
        });
        return R.ok();
    }
    @Transactional
    @Override
    public R cancelRole( List<Integer> ids) {
        Assert.notNull(ids, "ids is null");
        ids.forEach(id -> {
            Assert.notNull(id," id is null");
            sysUserRoleMapper.delete(id);
        });
        return R.ok();
    }

    @Override
    public R login(LoginDto loginDto) {
        Assert.notNull(loginDto,CommonConst.NULL_LOGIN_MSG);
        String userName = loginDto.getUserName();
        String phone = loginDto.getPhone();
        if (ObjectUtils.isEmpty(userName)&&ObjectUtils.isEmpty(phone)) {
            return  R.fail(CommonConst.ACCOUNT_NULL_MSG);
        }
        if (!ObjectUtils.isEmpty(userName)&&!ObjectUtils.isEmpty(phone)) {
            return  R.fail("账户名和手机号不能同时存在");
        }
        String vcode = loginDto.getVcode();
        SysUser sysUser = null;
        if (!ObjectUtils.isEmpty(userName)&&ObjectUtils.isEmpty(phone)) {
            sysUser=sysUserMapper.getUser(userName,"");
        }
        if (ObjectUtils.isEmpty(userName)&&!ObjectUtils.isEmpty(phone)) {

            if(!CommonUtil.phoneCorr(phone)){
                throw new CustomException(500,"手机号码格式错误");
            }
            String s = redisCacheClient.get(CommonConst.REDIS_VCODE_KEY+loginDto.getPhone());
            if(ObjectUtils.isEmpty(vcode)){
                return R.fail(CommonConst.CODE_NULL_MSG);
            }
            if(!vcode.equals(s)){
                return R.fail(CommonConst.CODE_ERROR_MSG);
            }
            sysUser=sysUserMapper.getUser("",phone);
        }
        if(ObjectUtils.isEmpty(sysUser)){
            return R.fail(CommonConst.USER_NULL_MSG);
        }

        String password =  MD5.create().digestHex16(loginDto.getPassword());
        if(!password.equals(sysUser.getPassword())){
            return R.fail(CommonConst.PWD_ERROR_MSG);
        }
        if(CommonConst.STOP_STATUS_CODE==sysUser.getStatus()){
            return R.fail(CommonConst.USER_LOCK_MSG);
        }
        boolean aBoolean = BooleanUtil.isTrue(redisCacheClient.getStringRedisTemplate().hasKey(CommonConst.REDIS_TOKEN_KEY + sysUser.getId()));
        if (aBoolean) return R.fail("您已登陆,不能重复登陆");
        //生成token,存入到redis中
        String token = TokenUtil.getInstance().creatToken(String.valueOf(sysUser.getId()), sysUser.getUserName());
        redisCacheClient.set(CommonConst.REDIS_TOKEN_KEY,sysUser.getId(),token,CommonConst.REDIS_TOKEN_EXPIRE, TimeUnit.SECONDS);
        Map<String,Object> map= new HashMap<>();
        map .put("token", token);
        return R.ok(map);
    }

    @Override
    public R logout(HttpServletRequest rq) {
        String token = rq.getHeader(CommonConst.TOKEN_NAME);
        R r = commonServer.checkToken(rq, CommonConst.TOKEN_NAME);
        if (r!=null) {
            return r;
        }
        String openId = TokenUtil.getInstance().getTokenDataOpenId(token);
        redisCacheClient.del(CommonConst.REDIS_TOKEN_KEY+openId);
        return R.ok();
    }
    @Transactional
    @Override
    public R register(RegisterDto registerDto) {
        SysUser user = sysUserMapper.getUserByPhone(registerDto.getPhone());
        if (!ObjectUtils.isEmpty(user)) throw  new CustomException(500,CommonConst.REGISTER_ERROR_MSG);
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(registerDto,sysUser);
        sysUser.setStatus(CommonConst.START_STATUS_CODE);
        sysUser.setPassword(MD5.create().digestHex16(sysUser.getPassword()));
        sysUserMapper.insert(sysUser);
        return R.ok();
    }

    @Override
    public R iconUpload( MultipartFile multipartFile) throws IOException {
        log.info("文件大小:{}",CommonUtil.bToKb(multipartFile.getSize()));
        String filename = multipartFile.getOriginalFilename();
        assert filename != null;
        String iconName = UUID.randomUUID()+filename.substring(filename.lastIndexOf("."),filename.length());
        String path=LocalDateTime.now().format(DateTimeFormatter.ISO_DATE).replace("-","/")+"/";
        String imgPath = IMG_PATH+path;
        File  file = new File(imgPath);
        if (!file.exists()) file.mkdirs();
        file = new File(imgPath+iconName);
        multipartFile.transferTo(file);
        Map<String,Object> map= new HashMap<>();
        map .put("iconPath", path  + iconName);
        return R.ok(map);
    }

    @Override
    public R sendCode(String phone) throws Exception {

        if(!CommonUtil.phoneCorr(phone)){
            throw new CustomException(500,"手机号码格式错误");
        }
        String vcode = CommonUtil.createVcode();
        if(PRINT_CODE) log.info("验证码:{}",vcode);
        boolean b = smsServer.sendMsg(phone, vcode);
        if(!b){
            return R.fail(CommonConst.CODE_SEND_ERROR_MSG);
        }
        redisCacheClient.set(CommonConst.REDIS_TOKEN_KEY,phone,vcode,CommonConst.REDIS_VCODE_EXPIRE,TimeUnit.SECONDS);
        return R.ok();
    }
    @Override
    public R getUserAndRoleAndPer(String userName){

        SysUser userAndRoleAndPer = sysUserMapper.getUserAndRoleAndPer(userName);
        return R.ok(userAndRoleAndPer);
    }

    @Override
    public R getUserRole( String userName) {
        return R.ok(sysUserMapper.getUserAndRole(userName));
    }

    @Override
    public R getIcon(String iconPath) {
        String iconPath1=IMG_PATH+iconPath;
        File file = new File(iconPath1);
        if (!file.exists()) throw new CustomException(RConst.ERROR_CODE,CommonConst.ICON_NULL_MSG);
        String imgBase64 = ImgUtils.imgToBase64(iconPath1);
        Map<String,Object> map = new HashMap<>();
        map.put("imgBase64",imgBase64);
        return R.ok(map);
    }
}