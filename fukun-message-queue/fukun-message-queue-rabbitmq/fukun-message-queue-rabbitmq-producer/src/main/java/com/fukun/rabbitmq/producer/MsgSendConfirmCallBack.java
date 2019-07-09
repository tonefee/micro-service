package com.fukun.rabbitmq.producer;

import com.fukun.commons.constants.RabbitMqConstants;
import com.fukun.rabbitmq.constant.Constants;
import com.fukun.rabbitmq.mapper.BrokerMessageLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import javax.annotation.Resource;
import java.util.Date;

import static com.fukun.rabbitmq.constant.Constants.MAX_TRY_COUNT;
import static com.fukun.rabbitmq.constant.Constants.MAX_TRY_COUNT_PREFIX_KEY;

/**
 * 消息发送到交换机确认机制
 * 如果设置了消息持久化，那么ack=true是在消息持久化完成后，就是存到硬盘上之后再发送的，
 * 确保消息已经存在硬盘上，万一消息服务挂了，消息服务恢复是能够再重发消息
 * 消息服务收到消息后，消息会处于"UNACK"的状态，直到客户端确认消息
 * 注意：一旦返回的确认消息丢失，那么消息服务会重发消息；如果你设置了autoAck= false，
 * 但又没应答 channel.baskAck，也没有应答 channel.baskNack，那么会导致非常严重的错误：
 * 消息队列会被堵塞住，可参考http://blog.sina.com.cn/s/blog_48d4cf2d0102w53t.html 所以，无论如何都必须应答
 *
 * @author tangyifei
 * @date 2019年7月6日11:53:58
 */
@Slf4j
public class MsgSendConfirmCallBack extends BaseCallBack {

    @Resource
    private BrokerMessageLogMapper brokerMessageLogMapper;

    /**
     * 当消息发送到交换机（exchange）时，该方法被调用.
     * 1.如果消息没有到exchange,则 ack=false
     * 2.如果消息到达exchange,则 ack=true
     *
     * @param correlationData 确认消息对比数据对象
     * @param ack             消息确认标志
     * @param cause           异常信息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (log.isInfoEnabled()) {
            log.info("消息的id（回调id）：{}", correlationData.getId());
        }
        if (ack) {
            if (log.isInfoEnabled()) {
                log.info("消息发送确认成功（消息发送到exchange成功）！");
            }
            brokerMessageLogMapper.changeBrokerMessageLogStatus(correlationData.getId(), Constants.ORDER_SEND_SUCCESS, new Date());
            try {
                // 如果消息发送到交换机成功，那么就清除缓存中的msgId 与 Message 的对应关系数据
                redisHandler.del(correlationData.getId());
                redisHandler.del(MAX_TRY_COUNT_PREFIX_KEY + correlationData.getId());
            } catch (Exception e) {
                if (log.isInfoEnabled()) {
                    log.error("缓存错误；{}", e);
                }
            }
        } else {
            // 如果消息发送到交换机失败，进行相关的重发机制（定时重发等机制）
            if (log.isInfoEnabled()) {
                log.error("消息发送确认失败（消息发送到exchange失败）：{}", cause);
            }
            int retryCount = (int) redisHandler.get(MAX_TRY_COUNT_PREFIX_KEY + correlationData.getId());
            if (log.isInfoEnabled()) {
                log.error("消息的重试次数：{}", retryCount);
            }
            // 如果重试次数大于小于3次，重新发送，大于最大重试次数还是失败，则可以通过人工进行干预。
            if (retryCount < MAX_TRY_COUNT) {
                // 增加重试次数
                redisHandler.incr(MAX_TRY_COUNT_PREFIX_KEY + correlationData.getId(), 1);
                Object cacheKey = null;
                try {
                    cacheKey = redisHandler.get(correlationData.getId());
                } catch (Exception e) {
                    if (log.isInfoEnabled()) {
                        log.error("缓存错误；{}", e);
                    }
                }
                if (null != cacheKey) {
                    Message message = (Message) cacheKey;
                    // 重新发送消息
                    rabbitTemplate.convertAndSend(RabbitMqConstants.TOPIC_EXCHANGE_NAME, RabbitMqConstants.OBJECT_ROUTING_KEY,
                            message, correlationData);
                }
            }
        }
    }

}
