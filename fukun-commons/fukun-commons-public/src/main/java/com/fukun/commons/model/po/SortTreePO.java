package com.fukun.commons.model.po;

/**
 * 基础排序树PO接口
 *
 * @author tangyifei
 * @since 2019-5-23 11:36:44 AM
 */
public interface SortTreePO<PK> extends TreePO<PK>, Comparable<SortTreePO> {

    Integer getSort();

    void setSort(Integer sort);

    @Override
    default int compareTo(SortTreePO sortTree) {
        if (sortTree == null) {
            return -1;
        }

        return Integer.compare(getSort() == null ? 0 : getSort(), sortTree.getSort() == null ? 0 : sortTree.getSort());
    }

}
