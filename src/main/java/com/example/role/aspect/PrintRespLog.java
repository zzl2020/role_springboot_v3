package com.example.role.aspect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface PrintRespLog {
    /**
     * 是否打印出参日志,true--打印,false--不打印,默认为true打印
     * */
    boolean status() default true;
}
