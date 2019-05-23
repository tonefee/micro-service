package com.fukun.commons.model.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

/**
 * 基础排序树PO类
 *
 * @author tangyifei
 * @since 2019-5-23 11:36:44 AM
 */
@Data
public abstract class BaseSortTreePO<PK> extends BaseTreePO<PK> implements SortTreePO<PK> {

    @ApiModelProperty(value = "排序值")
    @Column(name = "sort")
    private Integer sort;

}
