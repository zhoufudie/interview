package org.example.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * org.example
 * User: fd
 * Date: 2024/08/07 18:54
 * Description:
 * Version: V1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Role {

    int GUEST = 0;
    int USER = 1;
    int ADMIN = 2;

    public int value() default GUEST;

}
