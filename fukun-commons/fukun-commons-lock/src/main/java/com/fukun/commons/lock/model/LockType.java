package com.fukun.commons.lock.model;

/**
 * 锁类型
 *
 * @author tangyifei
 * @since 2019-5-23 15:30:25 PM
 */
public enum LockType {
    /**
     * 可重入锁
     */
    Reentrant,
    /**
     * 公平锁
     */
    Fair,
    /**
     * 读锁
     */
    Read,
    /**
     * 写锁
     */
    Write
}
