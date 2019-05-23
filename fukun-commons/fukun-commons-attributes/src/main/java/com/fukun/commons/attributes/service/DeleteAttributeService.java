package com.fukun.commons.attributes.service;

import com.fukun.commons.attributes.model.AttributesChange;

/**
 * 删除属性服务
 *
 * @author tangyifei
 * @since 2019-5-23 13:57:04 PM
 */
public interface DeleteAttributeService<OID> {

    /**
     * 删除单个属性
     *
     * @param objectId 对象id
     * @param key      属性key
     */
    AttributesChange<OID> deleteAttribute(OID objectId, String key);

    /**
     * 删除对象属性
     *
     * @param objectId 对象id
     */
    AttributesChange<OID> deleteAttributes(OID objectId);

    /**
     * 删除对象属性
     *
     * @param objectId 对象id
     * @param keys     属性keys
     */
    AttributesChange<OID> deleteAttributes(OID objectId, Iterable<String> keys);

}
