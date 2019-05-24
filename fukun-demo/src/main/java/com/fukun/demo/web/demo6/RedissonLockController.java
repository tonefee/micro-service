package com.fukun.demo.web.demo6;

import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.demo.model.bo.PayOrder;
import com.fukun.demo.service.pay.OrderPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分布式锁的测试
 */
@Slf4j
@ResponseResult
@RestController("demo6RedissonLockController")
@RequestMapping("demo6/lock/{id}")
public class RedissonLockController {

    @Autowired
    private OrderPaymentService orderPaymentService;

    @PostMapping
    boolean lock(@PathVariable("id") Long id) {
        PayOrder payOrder = PayOrder.builder()
                .orderId(id)
                .money(66.66D)
                .build();

        return orderPaymentService.pay(payOrder);
    }

}
