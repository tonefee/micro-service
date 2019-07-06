package com.fukun.rabbitmq.task;

import com.alibaba.fastjson.JSON;
import com.fukun.commons.util.CollectionUtil;
import com.fukun.rabbitmq.constant.Constants;
import com.fukun.rabbitmq.mapper.BrokerMessageLogMapper;
import com.fukun.rabbitmq.model.BrokerMessageLog;
import com.fukun.rabbitmq.model.Order;
import com.fukun.rabbitmq.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 消息重试，最大努力尝试策略（定时任务）
 *
 * @author tangyifei
 * @date 2019年7月5日17:09:23
 */
@Component
@Slf4j
public class RetryMessageTasker {

    private final static int MAX_TRY_COUNT = 3;

    @Resource
    private OrderService orderService;

    @Resource
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void reSend() {
        //pull status = 0 and timeout message
        List<BrokerMessageLog> list = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(messageLog -> {
                if (messageLog.getTryCount() >= MAX_TRY_COUNT) {
                    //update fail message
                    brokerMessageLogMapper.changeBrokerMessageLogStatus(messageLog.getMessageId(), Constants.ORDER_SEND_FAILURE, new Date());
                } else {
                    // resend
                    brokerMessageLogMapper.update4ReSend(messageLog.getMessageId(), new Date());
                    Order reSendOrder = JSON.parseObject(messageLog.getMessage(), Order.class);
                    try {
                        orderService.sendOrderMessage(reSendOrder);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (log.isInfoEnabled()) {
                            log.error("-----------异常处理-----------");
                        }
                    }
                }
            });
        }
    }
}
