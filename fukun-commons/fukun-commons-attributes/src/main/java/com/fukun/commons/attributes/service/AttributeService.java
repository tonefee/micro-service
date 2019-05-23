package com.fukun.commons.attributes.service;

/**
 * 通用属性服务
 *
 * @author tangyifei
 * @since 2019年5月23日13:56:28 PM
 */
public interface AttributeService<OID> extends
        InsertAttributeService<OID>,
        DeleteAttributeService<OID>,
        UpdateAttributeService<OID>,
        SelectAttributeService<OID> {
}
