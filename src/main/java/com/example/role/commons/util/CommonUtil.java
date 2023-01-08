package com.example.role.commons.util;

import cn.hutool.Hutool;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.RandomUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import com.example.role.commons.CommonConst;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    //生成6位验证码
    public  static String createVcode(){
        StringBuilder strB=new StringBuilder();
        for (int i = 0; i < 6; i++) {
            strB.append(RandomUtil.randomInt(10));
        }
        return strB.toString();
    }
    public static Client createClient(String accessKeyId, String accessKeySecret,String dnsName) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = dnsName;
        return new Client(config);
    }
    //字节转换其他单位
    public static String bToKb(long size){

        String a="";
        if(size<0){
            return "文件大小不能为负数";
        }
        if(size<1024){
         return  0+"b";
        }
        float fsize= size/1024;
        int num = 0;
        while (true){
            num++;
            if (fsize<1024){
                if(num==1){
                    a=fsize+"kb";
                }
                if(num==2){
                    a=fsize+"mb";
                }
                if(num==3){
                    a=fsize+"gb";
                }
                break;
            }
            fsize= fsize/1024;
        }
        return a;
    }
    /**
     * 判断手机格式是否正确
     * */
    public static boolean phoneCorr(String phone){
        Pattern compile = Pattern.compile(CommonConst.PHONE_REGEXP);
        Matcher matcher = compile.matcher(phone);
        return   matcher.find();
    }

    public static void main(String[] args) {
        Hutool.printAllUtils();
        EnumUtil
    }
}
