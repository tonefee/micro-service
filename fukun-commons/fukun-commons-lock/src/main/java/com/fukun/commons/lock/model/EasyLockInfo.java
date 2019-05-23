package com.fukun.commons.lock.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 锁基本信息
 *
 * @author tangyifei
 * @since 2019-5-23 15:29:51 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EasyLockInfo {

    private LockType type;

    private String name;

    private long waitTime;

    private long leaseTime;

}
