package com.example.role.commons.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R {
    private int code;
    private String msg;
    private Object data;
    public static R ok(){
        return  new R(RConst.SUCCESS_CODE,RConst.SUCCESS_MSG,null);
    }
    public static R ok(Object ob){
        return  new R(RConst.SUCCESS_CODE,RConst.SUCCESS_MSG,ob);
    }
    public static R fail(String msg){
        return  new R(RConst.ERROR_CODE,msg,null);
    }
    public static R fail(Object data){
        return  new R(RConst.ERROR_CODE,RConst.ERROR_MSG,data);
    }
    public static R fail(){
        return  new R(RConst.ERROR_CODE,RConst.SUCCESS_MSG,null);
    }
}
