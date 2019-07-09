package com.fukun.rabbitmq.service;

import com.alibaba.fastjson.JSON;
import com.fukun.commons.constants.RabbitMqConstants;
import com.fukun.rabbitmq.constant.Constants;
import com.fukun.rabbitmq.mapper.BrokerMessageLogMapper;
import com.fukun.rabbitmq.mapper.OrderMapper;
import com.fukun.rabbitmq.model.BrokerMessageLog;
import com.fukun.rabbitmq.model.Order;
import com.fukun.rabbitmq.producer.BaseCallBack;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.fukun.rabbitmq.constant.Constants.MAX_TRY_COUNT_PREFIX_KEY;

/**
 * 订单的业务逻辑
 *
 * @author tangyifei
 * @date 2019年7月5日16:34:21
 */
@Service
@Slf4j
public class OrderService extends BaseCallBack {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private BrokerMessageLogMapper brokerMessageLogMapper;

    /**
     * 创建订单
     *
     * @param order 订单实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Order order) {
        String orderId = System.currentTimeMillis() + "$" + UUID.randomUUID().toString();
        String msgId = System.currentTimeMillis() + "$" + UUID.randomUUID().toString();
        // 使用当前时间当做订单创建时间（为了模拟一下简化）
        Date orderTime = new Date();
        order.setId(orderId);
        order.setMessageId(msgId);
        orderMapper.insert(order);
        // 插入消息记录表数据
        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
        // 消息唯一ID
        brokerMessageLog.setMessageId(msgId);
        // 保存消息整体 转为JSON 格式存储入库
        brokerMessageLog.setMessage(JSON.toJSONString(JSON.toJSON(order)));
        // 设置消息状态为0 表示发送中
        brokerMessageLog.setStatus("0");
        // 设置消息未确认超时时间窗口为一分钟
        brokerMessageLog.setNextRetry(DateUtils.addMinutes(orderTime, Constants.ORDER_TIMEOUT));
        brokerMessageLog.setCreateTime(new Date());
        brokerMessageLog.setUpdateTime(new Date());
        brokerMessageLogMapper.insert(brokerMessageLog);
        redisHandler.set(MAX_TRY_COUNT_PREFIX_KEY + msgId, 0);
        // 发送消息
        sendOrderMessage(order);
    }

    /**
     * 发送消息
     *
     * @param order 订单实体
     */
    public void sendOrderMessage(Order order) {
        String msgId = order.getMessageId();
        Gson gson = new Gson();
        String json = gson.toJson(order);
        // 构建Message ,主要是使用 msgId 将 message 和 CorrelationData 关联起来。
        // 这样当消息发送到交换机失败的时候，在 MsgSendConfirmCallBack 中就可以根据
        // correlationData.getId()即 msgId,知道具体是哪个message发送失败,进而进行处理。
        // 将 msgId和 message绑定
        Message message = MessageBuilder.withBody(json.getBytes()).setContentEncoding("UTF-8")
                .setContentType(MessageProperties.CONTENT_TYPE_JSON).setCorrelationId(msgId).build();
        // 将 msgId和 CorrelationData绑定
        CorrelationData correlationData = new CorrelationData(msgId);
        // 将 msgId 与 Message 的关系保存起来,例如放到缓存中.
        try {
            redisHandler.set(msgId, gson.fromJson(gson.toJson(message), Map.class));
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.error("缓存错误：{}", e);
            }
        }
        // 当 MsgSendReturnCallback回调时（消息从交换机到队列失败）,进行处理 {@code MsgSendReturnCallback}.
        // 当 MsgSendConfirmCallBack回调时,进行处理 {@code MsgSendConfirmCallBack}.
        // 定时检查这个绑定关系列表,如果发现一些已经超时(自己设定的超时时间)未被处理,则手动处理这些消息.
        // 发送消息
        // 指定消息交换机  "amq.topic"
        // 指定队列key    "direct.queue"
        rabbitTemplate.convertAndSend(RabbitMqConstants.TOPIC_EXCHANGE_NAME, RabbitMqConstants.OBJECT_ROUTING_KEY,
                message, correlationData);
        if (log.isInfoEnabled()) {
            log.info("订单相关的消息发送完成，消息的id是: {}，发送的经过编码的消息是：{}，等待mq发送确认消息。", msgId, message);
        }
    }
}
