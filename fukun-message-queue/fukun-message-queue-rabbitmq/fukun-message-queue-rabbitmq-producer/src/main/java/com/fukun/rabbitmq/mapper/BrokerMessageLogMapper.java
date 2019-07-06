package com.fukun.rabbitmq.mapper;

import com.fukun.rabbitmq.model.BrokerMessageLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 消息记录持久层
 *
 * @author tangyifei
 * @date 2019年7月5日16:25:23
 */
public interface BrokerMessageLogMapper {

    /**
     * 插入消息记录
     *
     * @param brokerMessageLog 消息记录实体
     * @return 影响的行数
     */
    int insert(BrokerMessageLog brokerMessageLog);


    /**
     * 查询消息状态为0(发送中) 且已经超时的消息集合
     *
     * @return
     */
    List<BrokerMessageLog> query4StatusAndTimeoutMessage();

    /**
     * 重新发送统计count发送次数 +1
     *
     * @param messageId  消息记录主键
     * @param updateTime 更新时间
     */
    void update4ReSend(@Param("messageId") String messageId, @Param("updateTime") Date updateTime);

    /**
     * 更新最终消息发送结果 成功 or 失败
     *
     * @param messageId  消息记录主键
     * @param status     消息当前状态 0 发送中  1 发送成功  2 发送失败
     * @param updateTime 更新时间
     */
    void changeBrokerMessageLogStatus(@Param("messageId") String messageId, @Param("status") String status, @Param("updateTime") Date updateTime);
}
