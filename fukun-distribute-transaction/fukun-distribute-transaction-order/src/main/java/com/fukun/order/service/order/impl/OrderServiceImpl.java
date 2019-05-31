package com.fukun.order.service.order.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fukun.commons.service.impl.BaseMySqlCrudServiceImpl;
import com.fukun.order.client.StockClient;
import com.fukun.order.mapper.OrderMapper;
import com.fukun.order.model.po.OrderPO;
import com.fukun.order.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OrderServiceImpl extends BaseMySqlCrudServiceImpl<OrderPO, String> implements OrderService {

    private final static String TRANSACTION_MANAGER = "testTransactionManager";

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private StockClient stockClient;

    @Override
    @LcnTransaction
    @Transactional(value = TRANSACTION_MANAGER, noRollbackFor = Exception.class)
    public int addOrder(OrderPO orderPO) {
        // 减少库存操作
        try {
            stockClient.reduceStock();
        } catch (NullPointerException e) {
            // TODO 有待解决此处的空指针异常
            log.error("有待解决此处的空指针异常", e);
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        orderPO.setId(uuid);
        orderPO.setCreateTime(new Date());
        orderPO.setUpdateTime(new Date());
        int count = orderMapper.addOrder(orderPO);
        return count;
    }
}
