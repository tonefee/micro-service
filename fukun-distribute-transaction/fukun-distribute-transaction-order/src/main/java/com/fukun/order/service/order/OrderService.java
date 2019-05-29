package com.fukun.order.service.order;

import com.fukun.commons.service.CrudService;
import com.fukun.order.model.po.OrderPO;

/**
 * 订单服务接口
 *
 * @author tangyifei
 * @since 2019-5-24 15:32:20
 */
public interface OrderService extends CrudService<OrderPO, String> {

    /**
     * 添加订单
     *
     * @param orderPO
     * @return
     */
    int addOrder(OrderPO orderPO);

}
