package com.fukun.commons.lock.service.impl;

import com.fukun.commons.lock.model.EasyLockInfo;
import com.fukun.commons.lock.service.Lock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 写锁
 *
 * @author tangyifei
 * @since 2019-5-23 15:36:40 PM
 */
public class WriteLock implements Lock {

    private RReadWriteLock rLock;

    private EasyLockInfo easyLockInfo;

    private RedissonClient redissonClient;

    public WriteLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean acquire() {
        try {
            rLock = redissonClient.getReadWriteLock(easyLockInfo.getName());
            return rLock.writeLock().tryLock(easyLockInfo.getWaitTime(), easyLockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void release() {
        if (rLock.writeLock().isHeldByCurrentThread()) {
            rLock.writeLock().unlockAsync();
        }
    }

    @Override
    public Lock setLockInfo(EasyLockInfo easyLockInfo) {
        this.easyLockInfo = easyLockInfo;
        return this;
    }

}
