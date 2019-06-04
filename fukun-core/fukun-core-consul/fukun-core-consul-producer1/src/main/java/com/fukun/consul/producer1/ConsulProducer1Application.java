package com.fukun.consul.producer1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * consul生产端1
 *
 * @author tangyifei
 * @since 2019-6-4 14:44:37
 * @since jdk1.8
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsulProducer1Application {
    public static void main(String[] args) {
        SpringApplication.run(ConsulProducer1Application.class, args);
    }
}
