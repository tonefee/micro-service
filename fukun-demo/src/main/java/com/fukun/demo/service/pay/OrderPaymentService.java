package com.fukun.demo.service.pay;

import com.fukun.demo.model.bo.PayOrder;

/**
 * 付款订单服务接口
 *
 * @author tangyifei
 * @since 2019-5-24 15:29:33
 */
public interface OrderPaymentService {

    boolean pay(PayOrder payOrder);

}
