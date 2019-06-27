package com.fukun.consul.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 读取consul中的配置信息的类
 *
 * @author tangyifei
 * @since 2019年6月27日16:23:55
 */

@ConfigurationProperties(prefix = "test-config")
@Configuration
@Data
public class Config {

    private String testValue;

}
