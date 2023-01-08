package com.example.role.commons.util;

import com.example.role.commons.util.exception.CustomException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandle {
    //系统异常
    @ExceptionHandler(Exception.class)
    public R exceptionHandle(Exception e, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String,Object> map = new HashMap<>();
            fieldErrors.forEach(fieldError -> {
                assert fieldError != null;
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
            });
            return R.fail(map);
        }
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    public R customException(CustomException ce) {
        return R.fail(ce.getMessage());
    }
}
