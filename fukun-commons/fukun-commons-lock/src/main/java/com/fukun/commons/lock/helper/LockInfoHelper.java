package com.fukun.commons.lock.helper;

import com.fukun.commons.lock.annotation.EasyLock;
import com.fukun.commons.lock.config.RedissonProperties;
import com.fukun.commons.lock.model.EasyLockInfo;
import com.fukun.commons.lock.model.LockType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 锁基础信息处理器
 *
 * @author tangyifei
 * @since 2019-5-23 15:29:10 PM
 */
public class LockInfoHelper {

    public static final String LOCK_NAME_PREFIX = "lock";
    public static final String LOCK_NAME_SEPARATOR = ".";

    @Autowired
    private RedissonProperties redissonProperties;

    @Autowired
    private BusinessKeyHelper businessKeyHelper;

    public EasyLockInfo get(ProceedingJoinPoint joinPoint, EasyLock lock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LockType type = lock.lockType();
        String businessKeyName = businessKeyHelper.getKeyName(joinPoint, lock);
        String lockName = LOCK_NAME_PREFIX + LOCK_NAME_SEPARATOR + getName(lock.name(), signature) + businessKeyName;
        long waitTime = getWaitTime(lock);
        long leaseTime = getLeaseTime(lock);
        return new EasyLockInfo(type, lockName, waitTime, leaseTime);
    }

    private String getName(String annotationName, MethodSignature signature) {
        if (annotationName.isEmpty()) {
            return String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName());
        } else {
            return annotationName;
        }
    }


    private long getWaitTime(EasyLock lock) {
        return lock.waitTime() == Long.MIN_VALUE ?
                redissonProperties.getWaitTime() : lock.waitTime();
    }

    private long getLeaseTime(EasyLock lock) {
        return lock.leaseTime() == Long.MIN_VALUE ?
                redissonProperties.getLeaseTime() : lock.leaseTime();
    }

}
