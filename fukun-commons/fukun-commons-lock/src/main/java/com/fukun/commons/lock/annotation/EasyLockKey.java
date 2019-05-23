package com.fukun.commons.lock.annotation;

import java.lang.annotation.*;

/**
 * 分布式锁相关key的注解
 *
 * @author tangyifei
 * @since 2019-5-23 15:23:43 PM
 */
@Target(value = {ElementType.PARAMETER, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface EasyLockKey {
    String value() default "";
}
