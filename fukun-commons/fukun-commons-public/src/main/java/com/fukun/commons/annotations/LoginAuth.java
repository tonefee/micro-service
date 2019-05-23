package com.fukun.commons.annotations;

import java.lang.annotation.*;

/**
 * 已登录权限验证注解
 *
 * @author tangyifei
 * @since 2019-5-23 09:26:46 PM
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginAuth {

}
