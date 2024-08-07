package org.example.permission;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.List;

/**
 * com.gxa.op.permission
 * User: fd
 * Date: 2024/07/08 19:41
 * Description:注解管权限控制
 * Version: V1.0
 */
@Aspect//切面
@Slf4j
public class PermissionAdvice {

    //拦截所有的controller方法
    @Around("execution(* org.example.controller.*Controller.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        //1.获取目标方法上是否有权限的注解，如果有权限的注解，进行权限控制

        //2.获取目标方法
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        //3.获取目标方法上的权限注解
        Role role = method.getAnnotation(Role.class);

        //4.如果没有权限注解直接放行
        if(role==null){
            log.debug("{}方法不需要进行权限控制，直接放行...",method.getName());
            return joinPoint.proceed();
        }

        //获取目标方法上的注解的值
        int roleValue = role.value();
        String roleAnnotation=null;
        //判断注解角色
        if (roleValue==0){
            roleAnnotation = "guest";
        }else if(roleValue==1){
            roleAnnotation="user";
        }else {
            roleAnnotation="admin";
        }

        //4.判断用户是否拥有访问资源的权限  有就放行
        boolean isOk=false;

        if(roleAnnotation.equals(AdminThreadLocal.get().get("role"))){
            isOk=true;
        }


        if(!isOk){
            log.error("没有权限:{}",roleAnnotation);
            return "没有权限";
        }

        //有权限，直接放行
        log.debug("有权限,放行...");
        return joinPoint.proceed();
    }

}
