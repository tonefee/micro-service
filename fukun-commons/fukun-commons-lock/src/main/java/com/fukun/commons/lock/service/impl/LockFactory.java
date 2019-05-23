package com.fukun.commons.lock.service.impl;

import com.fukun.commons.lock.annotation.EasyLock;
import com.fukun.commons.lock.helper.LockInfoHelper;
import com.fukun.commons.lock.model.EasyLockInfo;
import com.fukun.commons.lock.model.LockType;
import com.fukun.commons.lock.service.Lock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 锁工厂
 *
 * @author tangyifei
 * @since 2019-5-23 15:32:43 PM
 */
@Slf4j
public class LockFactory {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private LockInfoHelper lockInfoProvider;

    private static final Map<LockType, Lock> lockMap = new HashMap<>();

    @PostConstruct
    public void init() {
        lockMap.put(LockType.Reentrant, new ReentrantLock(redissonClient));
        lockMap.put(LockType.Fair, new FairLock(redissonClient));
        lockMap.put(LockType.Read, new ReadLock(redissonClient));
        lockMap.put(LockType.Write, new WriteLock(redissonClient));
    }

    public Lock getLock(ProceedingJoinPoint joinPoint, EasyLock easyLock) {
        EasyLockInfo easyLockInfo = lockInfoProvider.get(joinPoint, easyLock);
        return lockMap.get(easyLockInfo.getType()).setLockInfo(easyLockInfo);
    }

}
