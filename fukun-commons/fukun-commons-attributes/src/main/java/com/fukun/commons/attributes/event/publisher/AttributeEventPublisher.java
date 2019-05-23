package com.fukun.commons.attributes.event.publisher;


import com.fukun.commons.attributes.model.AttributesChangedEvent;

/**
 * AttributeEventPublisher
 *
 * @param <OID>
 * @author tangyifei
 * @since 2019-5-23 13:39:07 PM
 */
public interface AttributeEventPublisher<OID> {

    /**
     * publishAttributesChangedEvent
     *
     * @param event     事件
     * @param tableName 表名
     */
    void publishAttributesChangedEvent(AttributesChangedEvent<OID> event, String tableName);

}
