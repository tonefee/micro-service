package com.fukun.commons.attributes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * AttributesChangedEvent
 *
 * @param <OID>
 * @author tangyifei
 * @since 2019-5-23 13:52:03 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributesChangedEvent<OID> implements Serializable {
    private static final long serialVersionUID = -5098574719305009319L;

    private AttributesChange<OID> data;

    private Date occurredTime;

}
