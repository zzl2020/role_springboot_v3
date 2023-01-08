package com.example.role.server;


import com.example.role.commons.util.R;
import com.example.role.entity.SysUser;
import com.example.role.entity.dto.LoginDto;
import com.example.role.entity.dto.RegisterDto;
import com.example.role.entity.dto.UpdateUserDto;
import com.example.role.entity.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

public interface SysUserServer {
	/**
	*添加数据
	**/
	int insert(SysUser sysUser);

	/**
	*根据id查询数据
	**/
	SysUser findById(Integer id);

	/**
	*查询所有数据
	**/
	List<SysUser> findAll();

	/**
	*修改数据
	**/
	R update(UpdateUserDto updateUserDto);

	/**
	*删除数据
	**/
	int delete(Integer id);

    R userAddRole( UserDto userDto);
	/**
	 * 取消角色权限
	 * */
	R cancelRole(List<Integer> ids);
	/**
	 * 登陆
	 * */
    R login(LoginDto loginDto);
	/**
	 * 登出
	 * */
	R logout(HttpServletRequest rq);
	/**
	 * 用户注册
	 * */
    R register(RegisterDto registerDto);
	/**
	 * 头像上传
	 * */
	R iconUpload(MultipartFile multipartFile) throws IOException;
	/**
	 * 发送验证码
	 * */
	R sendCode(String phone) throws Exception;
	/**
	 * 根据用户查询角色和权限
	 * */
	R getUserAndRoleAndPer(String userName);
	/**
	 * 根据用户查询角色
	 * */
	R getUserRole(String userName);

	R getIcon(String iconPath);
}
