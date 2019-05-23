package com.fukun.commons.service;

import com.fukun.commons.model.po.TreePO;

/**
 * 树结构crud服务
 *
 * @author tangyifei
 * @since 2019-5-23 16:09:54 PM
 */
public interface TreeCrudService<E extends TreePO, PK> extends
        CrudService<E, PK>,
        TreeSelectService<E, PK> {
}
