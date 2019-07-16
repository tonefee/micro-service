package com.fukun.jvm.model;

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
public class ApplicationMemReq implements Serializable {

    private static final long serialVersionUID = -1235197240077921309L;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 成员
     */
    private Integer mem;

}
