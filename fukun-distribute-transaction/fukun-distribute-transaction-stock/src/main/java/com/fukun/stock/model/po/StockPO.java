package com.fukun.stock.model.po;

import com.fukun.commons.model.po.BasePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

/**
 * 库存持久层对象
 *
 * @author tangyifei
 * @since 2019-5-24 09:42:13
 */
@ApiModel("库存持久层对象")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockPO extends BasePO<String> {

    private static final long serialVersionUID = -7491215402569546437L;

    @ApiModelProperty(value = "库存主键", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT REPLACE(UUID(),'-','')")
    @Length(min = 1, max = 64)
    private String id;

    @ApiModelProperty(value = "库存量", example = "1")
    @NotBlank
    private Integer stockNum;

}
