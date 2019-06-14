package com.fukun.consul.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.RestController;

/**
 * zuul网关服务
 * 启动类添加@EnableZuulProxy，支持网关路由
 *
 * @author tangyifei
 * @since 2019-6-11 17:34:01
 * @since jdk1.8
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@RestController
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }

    /**
     * 创建网关过滤器放到spring容器中
     *
     * @return 网关过滤器
     */
//    @Bean
//    public MyGatewayFilter myGatewayFilter() {
//        return new MyGatewayFilter();
//    }
}
