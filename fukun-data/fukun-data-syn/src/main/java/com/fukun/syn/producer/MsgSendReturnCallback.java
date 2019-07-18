package com.fukun.syn.producer;

import com.fukun.syn.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

/**
 * exchange到queue成功,则不回调return
 * exchange到queue失败,则回调return(需设置mandatory=true,否则不回调,消息就丢了)
 *
 * @author tangyifei
 * @date 2019年7月6日13:52:49
 */
@Slf4j
public class MsgSendReturnCallback extends BaseCallBack {

    /**
     * 当消息从交换机到队列失败时，该方法被调用。（若成功，则不调用）
     * confirmcallback用来确认消息是否有送达消息队列的判定标志
     * 如果消息没有到exchange,则confirm回调,ack=false 如果消息到达exchange,则confirm回调,ack=true 但如果是找不到exchange，则会先触发returncallback
     * 需要注意的是：该方法调用后，MsgSendConfirmCallBack中的confirm方法也会被调用，且ack = true
     *
     * @param message    消息实体
     * @param replyCode  回复状态码
     * @param replyText  回复文本
     * @param exchange   交换机
     * @param routingKey 路由KEY
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        if (log.isInfoEnabled()) {
            log.info("MsgSendReturnCallback [消息从交换机到队列失败]  message：{}", message);
            log.info("send message failed: replyCode：{}，replyText：{}", replyCode, replyText);
        }
        // 重新发送消息，可以使用定时机制重发消息等
        rabbitTemplate.convertAndSend(Constants.FANOUT_EXCHANGE_NAME, null, message);
    }

}
