package com.example.role.aspect;

import cn.hutool.json.JSONUtil;
import com.example.role.commons.CommonConst;
import com.example.role.commons.util.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.lang.reflect.Method;

/**
 * aop统一日志输出处理类
 * */
@Slf4j
//开启aop
@Aspect
@Component
public class WebLogAspect {
    /**
     * 以某某为切入点
     * */
    @Pointcut("@annotation(com.example.role.aspect.WebLog)")
    public void webLog(){
    }
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Exception {
        boolean status = getPrintStatus(joinPoint, CommonConst.PRINT_REQ_LOG_CLASS_NAME);
        if(!status){
            return;
        }
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String logDescription = getAspectLogDescription(joinPoint);
        log.info("==============================start=====================================");
        log.info("==========URL   {}",request.getRequestURL().toString());
        log.info("==========Description   {}",logDescription);
        log.info("==========HTTP Method   {}",request.getMethod());
        log.info("==========Class Method  {}.{}",joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName());
        log.info("==========IP            {}",request.getRemoteAddr());
        log.info("==========request body  {}",JSONUtil.toJsonStr(joinPoint.getArgs()));
    }
    @After("webLog()")
    public void doAfter (JoinPoint joinPoint) throws Exception {
        boolean status = getPrintStatus(joinPoint, null);
        if (!status){
            return;
        }
        log.info("==============================end========================================"+System.lineSeparator());
    }
    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws Exception
     */
    public String getAspectLogDescription(JoinPoint joinPoint)
            throws Exception {
        String[] name = getName(joinPoint);
        String targetName=name[0];
        String methodName=name[1];
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder("");
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description.append(method.getAnnotation(WebLog.class).desc());
                    break;
                }
            }
        }
        return description.toString();
    }
    @AfterThrowing(throwing = "ex",pointcut = "webLog()")
    public void doThrow(JoinPoint joinPoint,Exception ex){
       log.error("=========={} interface error msg:{}",joinPoint.getSignature().getName(),ex.getMessage());
    }
    @AfterReturning(pointcut = "webLog()",returning = "r")
    public void doReturn(JoinPoint joinPoint, R r) throws Exception {
        long startTime = System.currentTimeMillis();
        boolean s =  getPrintStatus(joinPoint, null);
        if (!s){
            return;
        }
        log.info("==========Response Args : {}", JSONUtil.toJsonStr(r));
        log.info("==========Time-Consuming : {} ms",System.currentTimeMillis()-startTime);
    }
    private String[] getName(JoinPoint joinPoint){
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        return new String[]{targetName,methodName};
    }
    private boolean getPrintStatus(JoinPoint joinPoint,String className) throws Exception {
        String[] name = getName(joinPoint);
        String targetName=name[0];
        String methodName=name[1];
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        boolean status=false;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    PrintReqLog pReqLog = method.getAnnotation(PrintReqLog.class);
                    PrintRespLog pRespLog = method.getAnnotation(PrintRespLog.class);
                    if("PrintReqLog".equals(className)){
                        if (!ObjectUtils.isEmpty(pReqLog)) {
                            status = pReqLog.status();
                        }
                        break;
                    }
                    if (!ObjectUtils.isEmpty(pRespLog)) {
                        status = pRespLog.status();
                    }

                    break;
                }
            }

    }
        return status;
    }
}
