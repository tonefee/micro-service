package com.fukun.commons.dao;

import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.select.*;
import tk.mybatis.mapper.common.condition.SelectByConditionMapper;
import tk.mybatis.mapper.common.condition.SelectCountByConditionMapper;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;
import tk.mybatis.mapper.common.ids.SelectByIdsMapper;

/**
 * 基础选择功能mapper
 *
 * @author tangyifei
 * @since 2019-5-22 14:52:31 PM
 */
public interface SelectMapper<T> extends Marker,
        SelectOneMapper<T>,
        tk.mybatis.mapper.common.base.select.SelectMapper<T>,
        SelectAllMapper<T>,
        SelectCountMapper<T>,
        SelectByPrimaryKeyMapper<T>,
        ExistsWithPrimaryKeyMapper<T>,
        SelectByIdsMapper<T>,
        SelectByConditionMapper<T>,
        SelectCountByConditionMapper<T>,
        SelectByExampleMapper<T> {
}
