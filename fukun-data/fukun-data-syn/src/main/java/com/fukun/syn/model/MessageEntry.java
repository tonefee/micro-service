package com.fukun.syn.model;

import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.*;

import java.io.Serializable;
import java.util.List;

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
     * 更新之前的数据记录列表
     */
    private List<BeforeDataRecord> beforeDataRecordList;

    /**
     * 更新之后的数据记录列表
     */
    private List<AfterDataRecord> afterDataRecordList;

    /**
     * 修改之前的数据库记录
     */
    @Getter
    @Setter
    @ToString(callSuper = true)
    public static class BeforeDataRecord extends BaseDataRecord {
    }

    /**
     * 修改之后的数据库记录
     */
    @Getter
    @Setter
    @ToString(callSuper = true)
    public static class AfterDataRecord extends BaseDataRecord {
    }

    /**
     * 数据库记录的父类
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class BaseDataRecord implements Serializable {

        private static final long serialVersionUID = 7189822638408740150L;

        /**
         * 列名
         */
        private String columnName;

        /**
         * 列值
         */
        private String columnValue;

        /**
         * 更新与否
         */
        private Boolean updateStatus;

    }


}
