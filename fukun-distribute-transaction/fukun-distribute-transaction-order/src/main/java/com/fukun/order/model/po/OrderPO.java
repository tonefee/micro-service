package com.fukun.order.model.po;

import com.fukun.commons.model.po.BasePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 订单持久层对象
 *
 * @author tangyifei
 * @since 2019-5-24 09:42:13
 */
@ApiModel("订单持久层对象")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderPO extends BasePO<String> {

    private static final long serialVersionUID = -7491215402569546437L;

    @ApiModelProperty(value = "订单主键", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT REPLACE(UUID(),'-','')")
    @Length(min = 1, max = 64)
    private String id;

    @ApiModelProperty(value = "订单号", example = "123456789")
    @NotBlank
    @Length(min = 1, max = 64)
    private String orderNo;

}
