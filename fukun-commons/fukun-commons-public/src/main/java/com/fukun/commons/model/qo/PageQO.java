package com.fukun.commons.model.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

/**
 * 分页查询对象
 *
 * @author tangyifei
 * @since 2019-5-23 11:47:25 AM
 */
@ApiModel("分页查询对象")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQO<T> {

    /**
     * 按创建时间倒序排序
     */
    public static final String ORDER_BY_CREATE_TIME_DESC = "create_time desc";

    @ApiModelProperty(value = "当前页号")
    @Range(min = 1, max = Integer.MAX_VALUE)
    private int pageNum = 1;

    @ApiModelProperty(value = "一页数量")
    @Range(min = 1, max = Integer.MAX_VALUE)
    private int pageSize = 10;

    @ApiModelProperty(value = "排序", notes = "例：create_time desc,update_time desc")
    private String orderBy;

    /**
     * 查询条件
     */
    private T condition;

    /**
     * 定义一个构造器，用于初始化当前页号，每页的大小
     *
     * @param pageNum  当前页号
     * @param pageSize 每页的大小
     */
    public PageQO(int pageNum, int pageSize) {
        super();
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /**
     * 计算偏移量
     *
     * @return
     */
    public int getOffset() {
        return (this.pageNum - 1) * this.pageSize;
    }

}
