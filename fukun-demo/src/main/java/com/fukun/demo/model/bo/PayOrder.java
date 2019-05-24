package com.fukun.demo.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 付款订单业务对象
 *
 * @author tangyifei
 * @since 2019-5-24 15:12:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayOrder {

    Long orderId;

    Double money;

    String randomNum;
}
