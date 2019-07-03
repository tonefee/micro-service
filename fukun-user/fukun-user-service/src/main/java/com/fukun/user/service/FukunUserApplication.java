package com.fukun.user.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用户模块启动类
 *
 * @author tangyifei
 * @since 2019-5-24 10:34:09
 */
@EnableFeignClients
@ServletComponentScan
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, RabbitAutoConfiguration.class, RedisAutoConfiguration.class})
@SpringBootApplication
@EnableDiscoveryClient
public class FukunUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(FukunUserApplication.class, args);
    }

}
