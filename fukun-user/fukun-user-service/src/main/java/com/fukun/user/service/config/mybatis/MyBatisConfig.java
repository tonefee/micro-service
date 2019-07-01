package com.fukun.user.service.config.mybatis;

import com.fukun.commons.dao.CrudMapper;
import com.fukun.user.service.config.db.DruidConfig;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

/**
 * mybatis配置类
 *
 * @author tangyifei
 * @since 2019-5-24 09:55:46
 */
@Configuration
@AutoConfigureAfter({MybatisAutoConfiguration.class, DruidConfig.class})
public class MyBatisConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        // 多数据源时，必须配置
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.fukun.user.service.mapper");
        // 直接继承了CrudMapper接口的才会被扫描，basePackage可以配置的范围更大。
        mapperScannerConfigurer.setMarkerInterface(CrudMapper.class);
        Properties properties = new Properties();
        properties.setProperty("mappers", CrudMapper.class.getName());
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");
        properties.setProperty("ORDER", "BEFORE");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }

}
