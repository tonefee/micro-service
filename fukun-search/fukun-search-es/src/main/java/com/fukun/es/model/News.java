package com.fukun.es.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 进行es相关单元测试的新闻类
 *
 * @author tangyifei
 * @date 2019年7月27日11:02:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    private String id;
    private String title;
    private String tag;
    private String publishTime;
}
