package com.fukun.rabbitmq.config.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

/**
 * 读取属性文件配置信息
 *
 * @author tangyifei
 * @date 2019年7月5日15:59:10
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource")
@PropertySource("classpath:druid.properties")
@Getter
@Setter
public class DruidDataSourceSettings {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    @Value("${druid.initialSize}")
    private int initialSize;

    @Value("${druid.minIdle}")
    private int minIdle;

    @Value("${druid.maxActive}")
    private int maxActive;

    @Value("${druid.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${druid.minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis;

    @Value("${druid.validationQuery}")
    private String validationQuery;

    @Value("${druid.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${druid.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${druid.testOnReturn}")
    private boolean testOnReturn;

    @Value("${druid.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${druid.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${druid.filters}")
    private String filters;

    @Value("${druid.connectionProperties}")
    private String connectionProperties;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigure() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
