package com.fukun.order.config.mybatis;

import com.fukun.commons.dao.CrudMapper;
import com.fukun.order.config.db.DruidConfig;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * mybatis配置
 *
 * @author tangyifei
 * @since 2019-5-24 14:12:43
 */
@Configuration
@AutoConfigureAfter({MybatisAutoConfiguration.class, DruidConfig.class})
public class MyBatisConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        // 多数据源时，必须配置
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        // mapper.java文件的路径
        mapperScannerConfigurer.setBasePackage("com.fukun.order.mapper");

        Properties properties = new Properties();
        properties.setProperty("mappers", CrudMapper.class.getName());
        // insert和update中，是否判断字符串类型!=''，少数方法会用到
        properties.setProperty("notEmpty", "false");
        // 数据库方言（主要用于：取回主键的方式）
        properties.setProperty("IDENTITY", "MYSQL");
        properties.setProperty("ORDER", "BEFORE");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }

}
