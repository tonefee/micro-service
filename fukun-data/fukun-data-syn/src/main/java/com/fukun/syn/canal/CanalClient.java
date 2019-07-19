package com.fukun.syn.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.fukun.syn.config.redis.RedisHandler;
import com.fukun.syn.constant.Constants;
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
        if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
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
        if (log.isInfoEnabled()) {
            log.info(String.format("================>>>>binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));
        }
        resultMap.put("logfileName", entry.getHeader().getLogfileName());
        resultMap.put("logfileOffset", entry.getHeader().getLogfileOffset());
        resultMap.put("schemaName", entry.getHeader().getSchemaName());
        resultMap.put("tableName", entry.getHeader().getTableName());
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
            org.springframework.amqp.core.Message message = MessageBuilder.withBody(json.getBytes()).setContentEncoding("UTF-8")
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON).setCorrelationId(msgId).build();
            CorrelationData correlationData = new CorrelationData(msgId);
            try {
                redisHandler.set(msgId, gson.fromJson(gson.toJson(message), Map.class));
            } catch (Exception e) {
                if (log.isInfoEnabled()) {
                    log.error("缓存错误：{}", e);
                }
            }
            rabbitTemplate.convertAndSend(Constants.FANOUT_EXCHANGE_NAME, null,
                    message, correlationData);
        }

    }

}
