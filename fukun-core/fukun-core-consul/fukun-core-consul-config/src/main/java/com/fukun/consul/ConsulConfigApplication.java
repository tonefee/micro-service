package com.fukun.consul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * consul的配置中心的相关启动类
 *
 * @author tangyifei
 * @since 2019-6-11 17:34:01
 * @since jdk1.8
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsulConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsulConfigApplication.class, args);
    }

}
