package com.fukun.syn.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * canal的客户端
 *
 * @author tangyifei
 * @date 2019年7月17日15:10:45
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "canal.server")
@Data
public class CanalClient {

    private String ip;

    private int port;

    private String dest;

    private String userName;

    private String userPass;

    public void createConnect() {

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
                    printEntry(message.getEntries());
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

    private static void printEntry(List<Entry> entrys) {
        for(Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
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
            for(RowData rowData : rowChange.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    if (log.isInfoEnabled()) {
                        log.info("------->>> 更新之前的行数据");
                    }
                    printColumn(rowData.getBeforeColumnsList());
                    if (log.isInfoEnabled()) {
                        log.info("------->>> 更新之后的行数据");
                    }
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static void printColumn(List<Column> columns) {
        for(Column column : columns) {
            if (log.isInfoEnabled()) {
                log.info(column.getName() + " : " + column.getValue() + "   update=" + column.getUpdated());
            }
        }
    }

}
