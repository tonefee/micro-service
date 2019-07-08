package com.fukun.rabbitmq.web;

import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.rabbitmq.constant.Constants;
import com.fukun.rabbitmq.model.Order;
import com.fukun.rabbitmq.producer.BaseCallBack;
import com.fukun.rabbitmq.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 消息发送方
 *
 * @author tangyifei
 * @date 2019年7月6日14:36:38
 */
@ResponseResult
@RestController("RabbitMqController")
@RequestMapping("/producer")
@Slf4j
@Api(value = "fukun-message-queue-rabbitmq-producer", tags = {"fukun-message-queue-rabbitmq-producer"})
public class RabbitMqController extends BaseCallBack {

    @Resource
    private OrderService orderService;

    @ApiOperation(value = "测试消息", httpMethod = "POST", notes = "测试消息")
    @PostMapping("/text")
    public void callbak(@RequestParam("message") @ApiParam(required = true, name = "message", value = "消息") String message) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        if (log.isInfoEnabled()) {
            log.info("消息的id：{}", correlationData.getId());
        }
        // 用RabbitMQ发送MQTT需将exchange配置为amq.topic
        rabbitTemplate.convertAndSend(Constants.TOPIC_EXCHANGE_NAME, Constants.BASIC_ROUTING_KEY, message, correlationData);
    }

    /**
     * 保存 order , 发送订单消息到消息队列，同时需要向库存服务发送通知减库存
     *
     * @param order 订单实体
     * @return 返回结果
     */
    @ApiOperation(value = "保存订单", httpMethod = "POST", notes = "保存订单")
    @PostMapping
    public void saveOrder(@RequestBody @ApiParam(value = "json格式", name = "订单对象", required = true) Order order) {
        orderService.createOrder(order);
    }

}
