package com.fukun.rabbitmq.config.rabbitmq;

import com.fukun.rabbitmq.constant.Constants;
import com.fukun.rabbitmq.producer.MsgSendConfirmCallBack;
import com.fukun.rabbitmq.producer.MsgSendReturnCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;

/**
 * rabbitmq 配置类
 *
 * @author tangyifei
 * @date 2019年7月5日14:05:26
 */
@Configuration
@Slf4j
public class RabbitMqConfiguration {

    @Resource
    private ConnectionFactory connectionFactory;

    @Bean
    public MessageConverter messageConverter() {
        return new SimpleMessageConverter() {
            @Override
            protected Message createMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
                Message message = super.createMessage(object, messageProperties);
                if (log.isInfoEnabled()) {
                    log.info("使用自定义的MessageConvert转换消息");
                }
                return message;
            }
        };
    }

    /**
     * 如果想将消息进行持久化，只需要将交换机和队列持久化就可以了
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(Constants.TOPIC_EXCHANGE_NAME);
    }

    /**
     * 如果想将消息进行持久化，只需要将交换机和队列持久化就可以了
     * 当我们在创建队列时指定durable = true，当服务重启的时候这个队列将会存活。也就是说队列被持久化了。
     * 同样的，交换机的durable也和队列同理
     * durable="true" 持久化消息队列 ， rabbitmq重启的时候不需要创建新的队列
     * auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
     * exclusive  表示该消息队列是否只在当前connection生效,默认是false
     */
    @Bean
    public Queue topicBasicQueue() {
//        Map<String, Object> arguments = new HashMap<>();
//        arguments.put("x-message-ttl", 60000);//60秒自动删除
//        return new Queue(QUEUE_NAME3,true,false,true,arguments);
        // 队列持久化
        return new Queue(Constants.TOPIC_QUEUE_NAME_BASIC, true);
    }

    @Bean
    public Queue topicObjectQueue() {
        // 队列持久化
        return new Queue(Constants.TOPIC_QUEUE_NAME_OBJECT, true);
    }

    @Bean
    public Binding topicBasicBinding() {
        return BindingBuilder.bind(topicBasicQueue()).to(topicExchange()).with(Constants.TOPIC_ROUTING_KEY_BASIC);
    }

    @Bean
    public Binding topicObjectBinding() {
        return BindingBuilder.bind(topicObjectQueue()).to(topicExchange()).with(Constants.TOPIC_ROUTING_KEY_OBJECT);
    }

    /**
     * 定义rabbit template用于数据的接收和发送,可以设置消息确认机制和回调:
     * 在生产者需要消息发送后的回调，需要对rabbitTemplate设置ConfirmCallback对象，
     * 由于不同的生产者需要对应不同的ConfirmCallback，如果rabbitTemplate设置为单例bean，则所有的rabbitTemplate
     * 实际的ConfirmCallback为最后一次申明的ConfirmCallback。
     * 通过使用RabbitTemplate来对开发者提供API操作, 因为要设置回调类，所以应是prototype类型，
     * 如果是singleton类型，则回调类为最后一次设置
     *
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        if (log.isInfoEnabled()) {
            log.info("加载rabbit模板类开始！");
        }
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        // 可以自定义消息转换器  默认使用的JDK的，所以消息对象需要实现Serializable
        template.setMessageConverter(messageConverter());
        // template.setMessageConverter(new Jackson2JsonMessageConverter());

        // 若使用confirm-callback或return-callback，
        // 必须要配置publisherConfirms或publisherReturns为true
        // 每个rabbitTemplate只能有一个confirm-callback和return-callback
        template.setConfirmCallback(msgSendConfirmCallBack());

        // 使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true，
        // 可针对每次请求的消息去确定’mandatory’的boolean值，
        // 只能在提供’return -callback’时使用，与mandatory互斥
        template.setReturnCallback(msgSendReturnCallback());
        template.setMandatory(true);
        if (log.isInfoEnabled()) {
            log.info("加载rabbit模板类结束！");
        }
        return template;
    }

    /**
     * 关于 msgSendConfirmCallBack 和 msgSendReturnCallback 的回调说明：
     * 如果消息没有到exchange,则confirm回调,ack=false
     * 如果消息到达exchange,则confirm回调,ack=true
     * exchange到queue成功,则不回调return
     * exchange到queue失败,则回调return(需设置mandatory=true,否则不回调,消息就丢了)
     * <p>
     * 消息确认机制:
     * Confirms给客户端一种轻量级的方式，能够跟踪哪些消息被broker处理，
     * 哪些可能因为broker宕掉或者网络失败的情况而重新发布。
     * 确认并且保证消息被送达，提供了两种方式：发布确认和事务。(两者不可同时使用)
     * 在channel为事务时，不可引入确认模式；同样channel为确认模式下，不可使用事务。
     *
     * @return
     */
    @Bean
    public MsgSendConfirmCallBack msgSendConfirmCallBack() {
        return new MsgSendConfirmCallBack();
    }

    @Bean
    public MsgSendReturnCallback msgSendReturnCallback() {
        return new MsgSendReturnCallback();
    }

}
