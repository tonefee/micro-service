package com.fukun.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单实体类
 *
 * @author tangyifei
 * @date 2019年7月5日15:43:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    private static final long serialVersionUID = -2632882972258755566L;

    private String id;

    private String name;

    private String messageId;
}
