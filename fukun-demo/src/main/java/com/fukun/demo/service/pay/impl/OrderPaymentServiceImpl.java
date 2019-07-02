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

    /**
     * <p>
     * 测试分布式锁，如果在 EasyLockAspectHandler 这个切面类中放行最后的释放锁代码，<br/>
     * 那么当下面的代码5秒后会自动释放锁，而不需要到达leaseTime的30秒后再次释放锁。<br/>
     * 没有释放锁之前，其他的进程只能等待，只有持有锁的进程释放锁后，其他的进程才能获取锁，并执行下面的代码。<br/>
     * </p>
     *
     * @param payOrder
     * @return
     */
    @EasyLock(name = "locks:order-payment:", keys = {"#payOrder.orderId"}, waitTime = 1, leaseTime = 30)
    @Override
    public boolean pay(PayOrder payOrder) {
        doSomething(5000);
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
