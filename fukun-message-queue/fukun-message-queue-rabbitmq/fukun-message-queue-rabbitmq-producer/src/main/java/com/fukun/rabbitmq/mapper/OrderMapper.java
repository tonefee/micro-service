package com.fukun.rabbitmq.mapper;

import com.fukun.rabbitmq.model.Order;

/**
 * 订单持久层
 *
 * @author tangyifei
 * @date 2019年7月5日16:37:45
 */
public interface OrderMapper {

    /**
     * 插入订单数据
     *
     * @param order 订单实体
     * @return 影响的行数
     */
    int insert(Order order);
}
