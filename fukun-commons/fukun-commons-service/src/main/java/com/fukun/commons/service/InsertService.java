package com.fukun.commons.service;

/**
 * 基础插入服务
 *
 * @author tangyifei
 * @since 2019-5-23 15:56:22 PM
 */
public interface InsertService<E, PK> {

    /**
     * 添加一条数据
     *
     * @param record 要添加的数据
     * @return 添加后生成的主键
     */
    PK insert(E record);
}
