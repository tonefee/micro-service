package com.fukun.commons.dao;

/**
 * 基础增删改查功能mapper
 *
 * @author tangyifei
 * @since 2019-5-22 14:48:44 PM
 */
public interface CrudMapper<T> extends
        InsertMapper<T>,
        DeleteMapper<T>,
        UpdateMapper<T>,
        SelectMapper<T> {
}
