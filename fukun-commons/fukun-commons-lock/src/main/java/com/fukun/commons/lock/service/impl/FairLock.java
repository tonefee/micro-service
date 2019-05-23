package com.fukun.commons.lock.service.impl;

import com.fukun.commons.lock.model.EasyLockInfo;
import com.fukun.commons.lock.service.Lock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 公平锁
 *
 * @author tangyifei
 * @since 2019-5-23 15:32:01 PM
 */
public class FairLock implements Lock {

    private RLock rLock;

    private EasyLockInfo easyLockInfo;

    private RedissonClient redissonClient;

    public FairLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean acquire() {
        try {
            rLock = redissonClient.getFairLock(easyLockInfo.getName());
            return rLock.tryLock(easyLockInfo.getWaitTime(), easyLockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void release() {
        if (rLock.isHeldByCurrentThread()) {
            rLock.unlockAsync();
        }
    }

    @Override
    public Lock setLockInfo(EasyLockInfo easyLockInfo) {
        this.easyLockInfo = easyLockInfo;
        return this;
    }
}
