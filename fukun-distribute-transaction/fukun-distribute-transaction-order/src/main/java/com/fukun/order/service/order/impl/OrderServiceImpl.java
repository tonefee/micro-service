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
    @Transactional(value = TRANSACTION_MANAGER, rollbackFor = Exception.class)
    public int addOrder(OrderPO orderPO) {
        // 减少库存操作，这里注意：如果reduceStock有返回值，
        // 可能会导致生成的代理类存在返回值的相关的强制类型转换。
        // 比如代理类中的代码，有返回值的话会有类型强转操作将null转为Integer再转为int，
        // 转换实际执行代码为((Integer)null).intValue()，所以会报控制针。
        // 详情请查看https://ask.csdn.net/questions/648332   java动态代理报 空指针异常。
        stockClient.reduceStock();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        orderPO.setId(uuid);
        orderPO.setCreateTime(new Date());
        orderPO.setUpdateTime(new Date());
        int count = orderMapper.addOrder(orderPO);
        return count;
    }
}
