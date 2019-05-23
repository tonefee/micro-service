package com.fukun.commons.dao;

import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper;
import tk.mybatis.mapper.common.condition.DeleteByConditionMapper;
import tk.mybatis.mapper.common.ids.DeleteByIdsMapper;

/**
 * 基础删除功能mapper
 * 
 * @author tangyifei
 * @since 2019-5-22 14:49:26 PM
 */
public interface DeleteMapper<T> extends Marker,
        tk.mybatis.mapper.common.base.delete.DeleteMapper<T>,
        DeleteByPrimaryKeyMapper<T>,
        DeleteByConditionMapper<T>,
        DeleteByIdsMapper<T> {
}
