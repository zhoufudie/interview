package org.example.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.permission.Role;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;

/**
 * org.example.config
 * User: fd
 * Date: 2024/08/07 19:06
 * Description:
 * Version: V1.0
 */
@Aspect
@Component
public class RoleAspect {

    private final HttpServletRequest request;

    public RoleAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Before("@annotation(org.example.permission.Role)")
    public void checkRole(JoinPoint joinPoint) throws AccessDeniedException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Role roleAnnotation = method.getAnnotation(Role.class);
        int requiredRole = roleAnnotation.value();

        // 从会话或JWT中获取用户角色，这里假设用户角色存储在会话中
        Integer userRole = (Integer) request.getSession().getAttribute("userRole");
        if (userRole == null) {
            userRole = Role.GUEST;
        }

        if (userRole < requiredRole) {
            throw new AccessDeniedException("权限不足，无法访问此资源");
        }
    }
}
