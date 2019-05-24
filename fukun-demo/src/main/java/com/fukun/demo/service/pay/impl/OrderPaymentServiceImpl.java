package com.fukun.demo.service.pay.impl;

import com.fukun.commons.lock.annotation.EasyLock;
import com.fukun.demo.model.bo.PayOrder;
import com.fukun.demo.service.pay.OrderPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 付款订单服务实现类
 *
 * @author tangyife
 * @since 2019-5-24 15:30:28
 */
@Slf4j
@Service
public class OrderPaymentServiceImpl implements OrderPaymentService {

    @EasyLock(name = "locks:order-payment:", keys = {"#payOrder.orderId"}, leaseTime = 1000)
    @Override
    public boolean pay(PayOrder payOrder) {
        doSomething(100);
        return true;
    }

    private void doSomething(long costTime) {
        try {
            Thread.sleep(costTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
