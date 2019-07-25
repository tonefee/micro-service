package com.fukun.es.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * es的相关查询参数
 *
 * @author tangyifei
 * @date 2019年7月25日11:18:23
 */
@ApiModel("es相关的查询对象")
@Data
@NoArgsConstructor
public class EsQuery {

    @ApiModelProperty(value = "索引名称", example = "fukun_order")
    private String index;

    @ApiModelProperty(value = "当前页", example = "0")
    private Integer page;

    @ApiModelProperty(value = "每页的大小", example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "排序字段", example = "score")
    private String sort;

    private Map<String, Object> queryMap;
}
