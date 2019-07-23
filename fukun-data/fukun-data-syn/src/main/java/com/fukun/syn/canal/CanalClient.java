package com.fukun.syn.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.fukun.commons.constants.RabbitMqConstants;
import com.fukun.syn.config.redis.RedisHandler;
import com.fukun.syn.constant.MessageComponentTypeEnums;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.*;

import static com.fukun.syn.constant.Constants.MAX_TRY_COUNT_PREFIX_KEY;

/**
 * canal的客户端
 *
 * @author tangyifei
 * @date 2019年7月17日15:10:45
 */
@Slf4j
@ConfigurationProperties(prefix = "canal.server")
@Data
@Component
public class CanalClient {

    private String ip;

    private int port;

    private String dest;

    private String userName;

    private String userPass;

    private int messageComponentType;

    public void createConnect(RabbitTemplate rabbitTemplate, RedisHandler redisHandler) {
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(ip, port), dest, userName, userPass);
        int batchSize = 1000;
        int emptyCount = 0;
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            int totalEmptyCount = 120;
            while (emptyCount < totalEmptyCount) {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(batchSize);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    if (log.isInfoEnabled()) {
                        log.info("empty count : {}", emptyCount);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                } else {
                    emptyCount = 0;
                    printEntry(message.getEntries(), rabbitTemplate, redisHandler);
                }
                // 提交确认
                connector.ack(batchId);
                // 处理失败, 回滚数据
                // connector.rollback(batchId);
            }
            if (log.isInfoEnabled()) {
                log.info("empty too many times, exit");
            }
        } finally {
            connector.disconnect();
        }
    }

    private void printEntry(List<Entry> entries, RabbitTemplate rabbitTemplate, RedisHandler redisHandler) {
        Map<String, Object> resultMap = new HashMap<>(1 << 3);
        // 如果实现随机访问的列表，那么使用普通for循环
        if (entries instanceof RandomAccess) {
            int size = entries.size();
            Entry entry;
            for(int i = 0; i < size; i++) {
                entry = entries.get(i);
                parseBinlogToMap(rabbitTemplate, redisHandler, resultMap, entry);
            }
        } else {
            Entry entry;
            for(Iterator<Entry> entryIterator = entries.iterator(); entryIterator.hasNext(); ) {
                entry = entryIterator.next();
                parseBinlogToMap(rabbitTemplate, redisHandler, resultMap, entry);
            }
        }

    }

    private void parseBinlogToMap(RabbitTemplate rabbitTemplate, RedisHandler redisHandler, Map<String, Object> resultMap, Entry entry) {
        EntryType entryType;
        if ((entryType = entry.getEntryType()) == EntryType.TRANSACTIONBEGIN || entryType == EntryType.TRANSACTIONEND) {
            return;
        }

        RowChange rowChange;
        try {
            rowChange = RowChange.parseFrom(entry.getStoreValue());
        } catch (Exception e) {
            throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                    e);
        }

        // 获取事件类型 UPDATE INSERT DELETE CREATE ALTER ERASE
        EventType eventType = rowChange.getEventType();
        Header header = entry.getHeader();
        String logfileName = header.getLogfileName();
        long logfileOffset = header.getLogfileOffset();
        String schemaName = header.getSchemaName();
        String tableName = header.getTableName();
        if (log.isInfoEnabled()) {
            log.info(String.format("================>>>>binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    logfileName, logfileOffset,
                    schemaName, tableName,
                    eventType));
        }
        resultMap.put("logfileName", logfileName);
        resultMap.put("logfileOffset", logfileOffset);
        resultMap.put("schemaName", schemaName);
        resultMap.put("tableName", tableName);
        for(RowData rowData : rowChange.getRowDatasList()) {
            if (eventType == EventType.DELETE) {
                resultMap.put("eventType", EventType.DELETE);
                printColumn(rowData.getBeforeColumnsList(), 1, resultMap);
            } else if (eventType == EventType.INSERT) {
                resultMap.put("eventType", EventType.INSERT);
                printColumn(rowData.getAfterColumnsList(), 1, resultMap);
            } else {
                resultMap.put("eventType", EventType.UPDATE);
                if (log.isInfoEnabled()) {
                    log.info("------->>> 更新之前的行数据");
                }
                printColumn(rowData.getBeforeColumnsList(), 0, resultMap);
                if (log.isInfoEnabled()) {
                    log.info("------->>> 更新之后的行数据");
                }
                printColumn(rowData.getAfterColumnsList(), 1, resultMap);
            }
        }
        // 发送消息到rabbitMq
        sendToRabbitmq(resultMap, rabbitTemplate, redisHandler);
    }

    private static void printColumn(List<Column> columns, int isNotBefore, Map<String, Object> resultMap) {
        int columnSize = columns.size();
        if (isNotBefore == 0) {
            Map<String, Object> beforeMap = new HashMap<>(columnSize);
            for(Column column : columns) {
                if (log.isInfoEnabled()) {
                    log.info(column.getName() + " : " + column.getValue() + "   update=" + column.getUpdated());
                }
                beforeMap.put(column.getName(), column.getValue());
            }
            resultMap.put("before", beforeMap);
        } else if (isNotBefore == 1) {
            Map<String, Object> afterMap = new HashMap<>(columnSize);
            for(Column column : columns) {
                if (log.isInfoEnabled()) {
                    log.info(column.getName() + " : " + column.getValue() + "   update=" + column.getUpdated());
                }
                afterMap.put(column.getName(), column.getValue());
            }
            resultMap.put("after", afterMap);
        }
    }

    private void sendToRabbitmq(Map<String, Object> resultMap, RabbitTemplate rabbitTemplate, RedisHandler redisHandler) {
        // 发送消息
        if (MessageComponentTypeEnums.RABBITMQ.getType() == messageComponentType) {
            String msgId = System.currentTimeMillis() + "$" + UUID.randomUUID().toString();
            redisHandler.set(MAX_TRY_COUNT_PREFIX_KEY + msgId, 0);
            Gson gson = new Gson();
            String json = gson.toJson(resultMap);
            // RabbitMQ 允许你对 message 和 queue 设置 TTL 值
            // 在RabbitMQ 3.0.0以后的版本中，TTL 设置可以具体到每一条 message 本身，对消息设置过期时间，expiration 字段以微秒为单位表示 TTL 值。
            // 且与 x-message-ttl 具有相同的约束条件。因为 expiration 字段必须为字符串类型，broker 将只会接受以字符串形式表达的数字。
            // 当同时指定了 queue 和 message 的 TTL 值，则两者中较小的那个才会起作用。
            // 虽然 consumer 从来看不到过期的 message ，但是在过期 message 到达 queue 的头部时确实会被真正的丢弃（或者 dead-lettered ）。
            // 当对每一个 queue 设置了 TTL 值时不会产生任何问题，因为过期的 message 总是会出现在 queue 的头部。
            // 当对每一条 message 设置了 TTL 时，过期的 message 可能会排队于未过期 message 的后面，直到这些消息被 consume 到或者过期了。
            // 在这种情况下，这些过期的 message 使用的资源将不会被释放，且会在 queue 统计信息中被计算进去（例如，queue 中存在的 message 的数量）。
            // 对于队列的TTL属性设置，即设置x-message-ttl，一旦消息过期，就会从队列中抹去，队列中已过期的消息肯定在队列头部，RabbitMQ只要定期从队头开始扫描是否有过期消息即可
            // 但是对于单个消息的ttl设置，即使消息过期，也不会马上从队列中抹去，因为每条消息是否过期是在即将投递到消费者之前判定的，
            // 每条消息的过期时间不同，如果要删除所有过期消息，势必要扫描整个队列，
            // 所以不如等到此消息即将被消费时再判定是否过期，如果过期，再进行删除。
            // 下面针对单个消息设置过期时间为10000，即publish 了最多能在 queue 中存活 10 秒的 message，这条消息发送到相应的队列之后，如果10秒内没有被消费，则变为死信。
            org.springframework.amqp.core.Message message = MessageBuilder.withBody(json.getBytes()).setContentEncoding("UTF-8")
                    .setExpiration("10000").setContentType(MessageProperties.CONTENT_TYPE_JSON).setCorrelationId(msgId).build();
            CorrelationData correlationData = new CorrelationData(msgId);
            try {
                redisHandler.set(msgId, gson.fromJson(gson.toJson(message), Map.class));
            } catch (Exception e) {
                if (log.isInfoEnabled()) {
                    log.error("缓存错误：{}", e);
                }
            }
            rabbitTemplate.convertAndSend(RabbitMqConstants.FANOUT_EXCHANGE_NAME, null,
                    message, correlationData);
        }

    }

}
