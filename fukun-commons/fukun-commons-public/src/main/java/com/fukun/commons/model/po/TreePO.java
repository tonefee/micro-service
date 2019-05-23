package com.fukun.commons.model.po;

/**
 * 树的持久层对象
 *
 * @param <PK>
 * @author tangyifei
 * @since 2019-5-23 11:43:26 AM
 */
public interface TreePO<PK> extends PO<PK> {

    /**
     * 获取父主键
     *
     * @return 数据类型
     */
    PK getParentId();

    /**
     * 设置父主键
     *
     * @param parentId 父主键
     */
    void setParentId(PK parentId);

}
