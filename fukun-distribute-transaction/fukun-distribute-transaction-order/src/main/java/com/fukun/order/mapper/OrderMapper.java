package com.fukun.order.mapper;

import com.fukun.commons.dao.CrudMapper;
import com.fukun.order.model.po.OrderPO;
import org.springframework.stereotype.Repository;

/**
 * 分布式事务订单模块基本的CRUD操作Mapper
 *
 * @author tangyifei
 * @since 2019-5-24 15:10:13
 */
@Repository
public interface OrderMapper extends CrudMapper<OrderPO> {

    /**
     * 添加订单
     *
     * @param orderPO 订单持久层对象
     * @return
     */
    int addOrder(OrderPO orderPO);
}
