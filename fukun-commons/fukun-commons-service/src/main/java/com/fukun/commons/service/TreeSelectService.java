package com.fukun.commons.service;

import com.fukun.commons.model.bo.Node;
import com.fukun.commons.model.po.TreePO;

import java.util.List;

/**
 * 树结构查看服务
 *
 * @author tangyifei
 * @since 2019-5-23 16:15:44 PM
 */
public interface TreeSelectService<E extends TreePO, PK> {

    /**
     * 根据父节点id获取子节点数据
     *
     * @param parentId 父节点ID
     * @return 子节点数据
     */
    List<E> selectChildren(PK parentId);

    /**
     * 获取当前节点下树数据
     *
     * @param parentId 父节点ID
     * @return 树信息
     */
    Node<E> selectNodeByParentId(PK parentId);

}
