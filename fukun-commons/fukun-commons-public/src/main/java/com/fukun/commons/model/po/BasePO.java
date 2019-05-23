package com.fukun.commons.model.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * 基础PO类
 *
 * @author tangyifei
 * @since 2019-5-23 11:28:45 PM
 */
@Data
public abstract class BasePO<PK> implements PO<PK> {

    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @Column(name = "update_time")
    private Date updateTime;

}
