package com.fukun.user.service.config.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 读取druid数据源属性
 *
 * @author tangyifei
 * @since 2019-5-24 09:51:38
 */
@ConfigurationProperties(prefix = "druid")
@Getter
@Setter
public class DruidProperties {
    private String url;
    private String username;
    private String password;
    private String driverClass;

    private int maxActive;
    private int minIdle;
    private int initialSize;
    private boolean testOnBorrow;
}
