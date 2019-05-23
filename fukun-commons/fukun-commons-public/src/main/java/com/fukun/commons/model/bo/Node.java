package com.fukun.commons.model.bo;

import com.fukun.commons.model.po.TreePO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 树节点
 *
 * @author tangyifei
 * @since 2019-5-23 10:50:35 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Node<E extends TreePO> {

    /**
     * 父节点
     */
    private E parent;

    /**
     * 子节点列表
     */
    private List<Node<E>> children;

}
