package com.fukun.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 暴露的库存相关的restful风格的api
 *
 * @author tangyifei
 * @since 2019-5-24 09:31:13
 */
@FeignClient(value = "fukun-stock", path = "stocks")
public interface StockClient {

    @PostMapping
    int reduceStock(@RequestParam("stockId") String stockId);
}