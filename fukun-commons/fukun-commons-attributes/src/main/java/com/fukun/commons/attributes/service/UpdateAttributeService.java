package com.fukun.commons.attributes.service;

import com.fukun.commons.attributes.model.AttributesChange;

import java.util.Map;

/**
 * 更新属性服务
 *
 * @author tangyifei
 * @since 2019-5-23 13:58:50 PM
 */
public interface UpdateAttributeService<OID> {

    /**
     * 设置对象属性
     *
     * @param objectId 对象id
     * @param key      属性key
     * @param value    属性值
     */
    AttributesChange<OID> setAttribute(OID objectId, String key, Object value);

    /**
     * 设置对象属性
     * 该操作将保存attributes中的属性，不存在于attributes中的属性将删除
     *
     * @param objectId   对象id
     * @param attributes 属性map，key：属性key，value：属性值
     */
    AttributesChange<OID> setAttributes(OID objectId, Map<String, Object> attributes);

}
