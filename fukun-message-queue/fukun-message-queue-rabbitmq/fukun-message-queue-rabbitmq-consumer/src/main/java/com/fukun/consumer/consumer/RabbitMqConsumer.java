package com.fukun.consumer.consumer;

import com.fukun.commons.constants.RabbitMqConstants;
import com.fukun.consumer.model.Order;
import com.fukun.consumer.service.StockService;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * rabbitMq消费端的消费消息的逻辑
 *
 * @author tangyifei
 * @date 2019年7月9日10:07:25
 */
@Component
@Slf4j
public class RabbitMqConsumer {

    @Resource
    private StockService stockService;

    /**
     * queues 指定从哪个队列（queue）订阅消息
     * 第一个参数 deliveryTag：就是接受的消息的deliveryTag,可以通过msg.getMessageProperties().getDeliveryTag()获得
     * 第二个参数 multiple：如果为true，确认之前接受到的消息；如果为false，只确认当前消息。
     * 如果为true就表示连续取得多条消息才会发确认，和计算机网络的中tcp协议接受分组的累积确认十分相似，
     * 能够提高效率。
     * 同样的，如果要nack或者拒绝消息（reject）的时候，
     * 也是调用channel里面的basicXXX方法就可以了（要指定tagId）。
     * 注意：如果抛异常或nack（并且requeue为true），消息会重新入队列，
     * 并且会造成消费者不断从队列中读取同一条消息的假象。
     *
     * @param message 消息
     * @param channel 通道
     */
    @RabbitListener(queues = {RabbitMqConstants.TOPIC_QUEUE_NAME_BASIC})
    public void handleBasicMessage(Message message, Channel channel) throws IOException {
        try {
            // 处理消息
            if (log.isInfoEnabled()) {
                log.info("消费者处理消息成功，消息是：{}", new String(message.getBody(), "UTF-8"));
            }
            // 执行减库存操作，注意保证减库存接口的幂等性
            stockService.reduceStock(new Gson().fromJson(new String(message.getBody()), Order.class));

            // 确认消息
            // 如果 channel.basicAck   channel.basicNack  channel.basicReject 这三个方法都不执行，消息也会被确认 【这个其实并没有在官方看到，不过自己测试的确是这样哈】
            // 所以，正常情况下一般不需要执行 channel.basicAck
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.error("消费者处理消息失败，消息是：{}，异常是：{}", new String(message.getBody(), "UTF-8"), e);
            }
            // 处理消息失败，将消息重新放回队列，但是消费端处理失败的消息无法进入死信队列中
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    @RabbitListener(queues = {RabbitMqConstants.TOPIC_QUEUE_NAME_OBJECT})
    public void handleObjectMessage(Message message, Channel channel) throws IOException {
        try {
            if (log.isInfoEnabled()) {
                // 处理消息
                log.info("消费者处理消息成功，消息是：{}", new String(message.getBody()));
            }
            // 执行减库存操作，注意保证减库存接口的幂等性
            if (null != message) {
                stockService.reduceStock(new Gson().fromJson(new String(message.getBody()), Order.class));
            }
            // 确认消息
            // 如果 channel.basicAck   channel.basicNack  channel.basicReject 这三个方法都不执行，消息也会被确认 【这个其实并没有在官方看到，不过自己测试的确是这样哈】
            // 所以，正常情况下一般不需要执行 channel.basicAck
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.error("消费者处理消息失败，消息是：{}，异常是：{}", new String(message.getBody()), e);
            }
            // 处理消息失败，将消息重新放回队列，但是消费端处理失败的消息无法进入死信队列中
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    /**
     * 监听替补队列 来验证死信.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = {RabbitMqConstants.DEAD_LETTER_REDIRECT_QUEUE_NAME})
    public void redirect(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        if (log.isInfoEnabled()) {
            log.info("dead message  10s 后 消费消息 {}", new String(message.getBody()));
        }
    }

}
