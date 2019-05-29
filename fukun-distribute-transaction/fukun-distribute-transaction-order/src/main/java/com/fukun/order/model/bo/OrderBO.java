package com.fukun.order.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单业务对象
 *
 * @author tangyifei
 * @since 2019-5-24 15:12:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBO {

    /**
     * 订单主键
     */
    Long id;

    /**
     * 订单号
     */
    String orderNo;
}
