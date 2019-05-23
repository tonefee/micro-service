package com.fukun.commons.lock.annotation;

import com.fukun.commons.lock.model.LockType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author tangyifei
 * @since 2019-5-23 14:19:12 PM
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EasyLock {

    /**
     * 锁的名称
     */
    String name() default "";

    /**
     * 自定义业务key
     */
    String[] keys() default {};

    /**
     * 锁类型，默认为可重入锁
     */
    LockType lockType() default LockType.Reentrant;

    /**
     * 最多等待时间（默认单位：秒）
     */
    long waitTime() default 30;

    /**
     * 自动解锁时间（默认单位：秒）
     */
    long leaseTime() default -1;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
