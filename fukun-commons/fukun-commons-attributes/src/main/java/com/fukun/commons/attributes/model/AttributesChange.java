package com.fukun.commons.attributes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * AttributesChange
 *
 * @param <OID>
 * @author tangyifei
 * @since 2019-5-23 13:51:18 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributesChange<OID> implements Serializable {
    private static final long serialVersionUID = -5008407345712737581L;

    private String objectType;

    private OID objectId;

    private Map<String, AttributeChange> added;

    private Map<String, AttributeChange> updated;

    private Map<String, AttributeChange> removed;

}
