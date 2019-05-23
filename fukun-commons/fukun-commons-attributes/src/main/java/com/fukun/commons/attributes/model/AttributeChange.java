package com.fukun.commons.attributes.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * AttributeChange
 *
 * @author tangyifei
 * @since 2019-5-23 13:50:25 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeChange implements Serializable {
    private static final long serialVersionUID = -662090239071614840L;

    private Object previous;

    private Object current;

}