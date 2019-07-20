package com.fukun.syn.model;

import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * binlog事件消息
 *
 * @author tangyifei
 * @since 2019年7月18日10:54:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntry implements Serializable {

    private static final long serialVersionUID = 8065186069553271406L;

    /**
     * 日志文件名称
     */
    private String logfileName;

    /**
     * 日志偏移量
     */
    private Long logfileOffset;

    /**
     * 数据库名称
     */
    private String schemaName;

    /**
     * 数据库相关的表名
     */
    private String tableName;

    /**
     * binlog日志类型
     */
    private CanalEntry.EventType eventType;

    /**
     * 更新之前的数据hash表
     */
    private Map<String, Object> before;

    /**
     * 更新之后的数据hash表
     */
    private Map<String, Object> after;

}
