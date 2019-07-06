package com.fukun.rabbitmq.producer;

import com.fukun.rabbitmq.config.redis.RedisHandler;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.Resource;

/**
 * 消息确认机制回调，确保生产者到mq的消息不丢失
 *
 * @author tangyifei
 * @date 2019年7月6日13:44:47
 */
public class BaseCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    /**
     * 自动注入RabbitTemplate模板类
     */
    public static RabbitTemplate rabbitTemplate;

    /**
     * 自动注入redis操作相关的bean
     */
    @Resource
    protected RedisHandler redisHandler;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

    }
}
