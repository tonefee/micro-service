package com.fukun.demo.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderCreateServiceImpl implements OrderCreateService {

    //	@EasyLock(name = "order", keys = "#orderId", waitTime = 120, leaseTime = 10, timeUnit = TimeUnit.SECONDS)
    @Override
    public void create(String orderId) {
        log.info("order create start, orderId:{}", orderId);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("order create end, orderId:{}", orderId);
    }

}
