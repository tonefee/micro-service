package com.fukun.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户实体类
 * 这个实体类需要实现序列化接口
 *
 * @author tangyifei
 * @date 2019年7月5日14:15:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = -7647781543234105367L;
    private Integer userId;
    private String username;
    private String password;
}
