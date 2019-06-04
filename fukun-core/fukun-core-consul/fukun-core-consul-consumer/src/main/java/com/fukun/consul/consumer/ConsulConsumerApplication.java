package com.fukun.consul.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * consul消费端
 *
 * @author tangyifei
 * @since 2019-6-4 14:44:37
 * @since jdk1.8
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.fukun"})
public class ConsulConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsulConsumerApplication.class, args);
    }
}
