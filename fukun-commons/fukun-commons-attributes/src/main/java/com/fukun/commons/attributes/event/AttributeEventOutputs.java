package com.fukun.commons.attributes.event;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * AttributeEventOutputs
 *
 * @author tangyifei
 * @since 2019-5-23 13:46:41 PM
 */
public interface AttributeEventOutputs {

    @Output(AttributeEventChannels.ATTRIBUTES)
    MessageChannel posted();

}