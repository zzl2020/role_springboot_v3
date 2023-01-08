package com.example.role.aspect;

import java.lang.annotation.*;

/**
 * 指定运行时状态
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface WebLog {
    String desc() default "";
}
