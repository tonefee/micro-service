package com.fukun.commons.attributes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Attribute
 *
 * @param <OID>
 * @author tangyifei
 * @since 2019-5-23 13:49:19 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attribute<OID> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private OID objectId;

    private String key;

    private String value;

    private String type;

}
