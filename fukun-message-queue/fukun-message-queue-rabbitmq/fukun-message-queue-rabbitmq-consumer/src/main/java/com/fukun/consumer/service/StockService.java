package com.fukun.consumer.service;

import com.alibaba.fastjson.JSONObject;
import com.fukun.consumer.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 库存服务接口
 *
 * @author tangyifei
 * @since 2019-5-24 15:32:20
 */
@Service
@Slf4j
public class StockService {

    /**
     * 获取订单消息，减少库存
     *
     * @param order 订单实体
     * @return
     */
    public int reduceStock(Order order) {
        // 这里不做实现
        if (log.isInfoEnabled()) {
            log.info("获取的订单信息是：{}", JSONObject.toJSON(order));
        }
        return 0;
    }

}
