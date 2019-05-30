package com.fukun.order.web;

import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.order.model.po.OrderPO;
import com.fukun.order.service.order.OrderService;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单控制层
 *
 * @author tangyifei
 * @since 2019-5-24 15:48:53
 */
@ResponseResult
@RestController("OrderController")
@RequestMapping("orders")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public int addOrder(@Validated @RequestBody OrderPO orderPO) {
        return orderService.addOrder(orderPO);
    }

}
