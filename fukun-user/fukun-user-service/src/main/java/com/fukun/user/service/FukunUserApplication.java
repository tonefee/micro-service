package com.fukun.user.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 用户模块启动类
 *
 * @author tangyifei
 * @since 2019-5-24 10:34:09
 */
@EnableEurekaClient
@EnableFeignClients
@ServletComponentScan
@SpringBootApplication
public class FukunUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(FukunUserApplication.class, args);
    }

}
