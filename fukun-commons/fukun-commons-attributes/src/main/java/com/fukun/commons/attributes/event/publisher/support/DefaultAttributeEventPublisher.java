package com.fukun.commons.attributes.event.publisher.support;

import com.fukun.commons.attributes.event.AttributeEventOutputs;
import com.fukun.commons.attributes.event.publisher.AttributeEventPublisher;
import com.fukun.commons.attributes.model.AttributesChangedEvent;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * DefaultAttributeEventPublisher
 *
 * @param <OID>
 * @author tangyifei
 * @since 2019-5-23 13:40:59 PM
 */
@Slf4j
@Component
public class DefaultAttributeEventPublisher<OID> implements AttributeEventPublisher<OID> {

    @Autowired
    private AttributeEventOutputs outputs;

    @Override
    public void publishAttributesChangedEvent(AttributesChangedEvent<OID> event, String tableName) {
        String routingKey = "attributes." + tableName;
        outputs.posted().send(toMessage(routingKey, event));
        log.debug("attributes_changement_event,routingKey={},event={}", routingKey, event);
    }

    private Message<?> toMessage(String routingKey, Object obj) {
        Map<String, Object> headers = Maps.newHashMap();
        headers.put("routingKey", routingKey);
        return MessageBuilder.withPayload(obj).copyHeaders(headers).build();
    }

}
