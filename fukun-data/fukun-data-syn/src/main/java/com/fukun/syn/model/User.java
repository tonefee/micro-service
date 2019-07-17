package com.fukun.syn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户测试类
 *
 * @author tangyifei
 * @since 2019年7月12日16:21:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 7245959315196834961L;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户年龄
     */
    private Integer userAge;

    /**
     * 用户性别
     */
    private String userSex;
}
