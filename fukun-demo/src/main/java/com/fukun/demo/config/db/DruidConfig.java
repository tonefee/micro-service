package com.fukun.demo.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * druid数据源相关的配置类
 *
 * @author tangyifei
 * @since 2019-5-24 14:07:29
 */
@Configuration
@EnableConfigurationProperties(DruidConfig.DruidProperties.class)
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(prefix = "druid", name = "url")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class DruidConfig {

    @Autowired
    private DruidProperties properties;

    /**
     * 注册druid数据源相关的bean到spring容器中
     *
     * @return druid数据源
     */
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        if (properties.getInitialSize() > 0) {
            dataSource.setInitialSize(properties.getInitialSize());
        }
        if (properties.getMinIdle() > 0) {
            dataSource.setMinIdle(properties.getMinIdle());
        }
        if (properties.getMaxActive() > 0) {
            dataSource.setMaxActive(properties.getMaxActive());
        }
        dataSource.setTestOnBorrow(properties.isTestOnBorrow());
        try {
            dataSource.init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }

    /**
     * 读取druid数据源相关的属性的类
     *
     * @author tangyifei
     * @since 2019-5-24 14:09:20
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
}
