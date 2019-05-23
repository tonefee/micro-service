package com.fukun.commons.lock.service;


import com.fukun.commons.lock.model.EasyLockInfo;

/**
 * 锁上层接口
 *
 * @author tangyifei
 * @since 2019-5-23 15:31:34 PM
 */
public interface Lock {

    Lock setLockInfo(EasyLockInfo easyLockInfo);

    boolean acquire();

    void release();
}
