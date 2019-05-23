package com.fukun.commons.model.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

/**
 * 基础树PO类
 *
 * @author tangyifei
 * @since 2019-5-23 11:42:30 PM
 */
@Data
public abstract class BaseTreePO<PK> extends BasePO<PK> implements TreePO<PK> {

    @ApiModelProperty(value = "父ID")
    @Column(name = "parent_id")
    private PK parentId;

}
