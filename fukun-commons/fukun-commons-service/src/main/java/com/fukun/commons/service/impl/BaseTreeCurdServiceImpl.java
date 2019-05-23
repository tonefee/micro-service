package com.fukun.commons.service.impl;

import com.fukun.commons.model.bo.Node;
import com.fukun.commons.model.po.TreePO;
import com.fukun.commons.service.TreeCrudService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 排序树通用CRUD服务类
 *
 * @author tangyifei
 * @since 2019-5-23 15:52:35 PM
 */
@Slf4j
public abstract class BaseTreeCurdServiceImpl<E extends TreePO<PK>, PK> extends BaseMySqlCrudServiceImpl<E, PK> implements TreeCrudService<E, PK> {

    private static int MAX_TREE_HIGH = 10;

    @Override
    public List<E> selectChildren(PK parentId) {
        Assert.notNull(parentId, "parentId is null");

        try {
            E e = poType.newInstance();
            e.setParentId(parentId);
            return crudMapper.select(e);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("selectChildren occurs error, caused by: ", e);
            throw new RuntimeException("selectChildren occurs error", e);
        }
    }

    @Override
    public Node<E> selectNodeByParentId(PK parentId) {
        Assert.notNull(parentId, "parentId is null");

        int currentTreeHigh = 1;

        Node<E> tree = new Node<>();
        E parent = super.selectByPk(parentId);
        if (parent != null) {
            Node<E> eNode = wrapNode(parent);
            tree = buildTree(eNode, currentTreeHigh);
        }

        return tree;
    }

    private Node<E> buildTree(Node<E> eNode, int currentTreeHigh) {
        if (currentTreeHigh++ >= MAX_TREE_HIGH) {
            return eNode;
        }

        List<Node<E>> descendantNodes = getDescendantNodes(eNode.getParent().getId());
        List<Node<E>> children = eNode.getChildren() == null ? Lists.newArrayList() : eNode.getChildren();
        children.addAll(descendantNodes);
        eNode.setChildren(children);

        for(Node<E> node : descendantNodes) {
            buildTree(node, currentTreeHigh);
        }

        return eNode;
    }

    private List<Node<E>> getDescendantNodes(PK id) {
        List<E> eList = this.selectChildren(id);

        List<Node<E>> list = Lists.newLinkedList();
        for(E parent : eList) {
            Node<E> node = wrapNode(parent);
            list.add(node);
        }

        return list;
    }

    private Node<E> wrapNode(E parent) {
        Node<E> node = new Node<>();
        node.setParent(parent);
        return node;
    }
}
