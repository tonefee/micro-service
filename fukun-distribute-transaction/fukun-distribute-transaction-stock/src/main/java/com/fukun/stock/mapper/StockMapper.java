package com.fukun.stock.mapper;

import com.fukun.commons.dao.CrudMapper;
import com.fukun.stock.model.po.StockPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 分布式事务库存模块基本的CRUD操作Mapper
 *
 * @author tangyifei
 * @since 2019-5-24 15:10:13
 */
@Repository
public interface StockMapper extends CrudMapper<StockPO> {

    /**
     * 减少库存
     *
     * @return
     */
    int reduceStock(@Param("stockId") String stockId);
}
