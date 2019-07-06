package com.fukun.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 消息记录实体
 *
 * @author tangyifei
 * @date 2019年7月5日15:46:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrokerMessageLog {
    private String messageId;

    private String message;

    private Integer tryCount;

    private String status;

    private Date nextRetry;

    private Date createTime;

    private Date updateTime;
}
