package com.fukun.order.service.order.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fukun.commons.service.impl.BaseMySqlCrudServiceImpl;
import com.fukun.order.client.StockClient;
import com.fukun.order.mapper.OrderMapper;
import com.fukun.order.model.po.OrderPO;
import com.fukun.order.service.order.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * 订单服务接口实现
 *
 * @author tangyifei
 * @since 2019-5-24 15:33:14
 */
@Service
public class OrderServiceImpl extends BaseMySqlCrudServiceImpl<OrderPO, String> implements OrderService {

    private final static String TRANSACTION_MANAGER = "testTransactionManager";

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private StockClient stockClient;

    @Override
    @LcnTransaction
    @Transactional(value = TRANSACTION_MANAGER, rollbackFor = Exception.class)
    public int addOrder(OrderPO orderPO) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        orderPO.setId(uuid);
        orderPO.setCreateTime(new Date());
        orderPO.setUpdateTime(new Date());
        int count = orderMapper.addOrder(orderPO);
        // 减少库存操作
        stockClient.reduceStock("1");
        return count;
    }
}
