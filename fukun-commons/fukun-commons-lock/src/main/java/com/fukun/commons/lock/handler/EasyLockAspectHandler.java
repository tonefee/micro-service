package com.fukun.commons.lock.handler;

import com.fukun.commons.lock.annotation.EasyLock;
import com.fukun.commons.lock.service.Lock;
import com.fukun.commons.lock.service.impl.LockFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 锁注解切面处理
 *
 * @author tangyifei
 * @since 2019-5-23 15:27:12 PM
 */
@Aspect
@Component
public class EasyLockAspectHandler {

    @Autowired
    LockFactory lockFactory;

    @Around(value = "@annotation(easyLock)")
    public Object around(ProceedingJoinPoint joinPoint, EasyLock easyLock) throws Throwable {
        Lock lock = lockFactory.getLock(joinPoint, easyLock);
        boolean currentThreadLock = false;
        try {
            currentThreadLock = lock.acquire();
            return joinPoint.proceed();
        } finally {
            if (currentThreadLock) {
//                lock.release();
            }
        }
    }
}
