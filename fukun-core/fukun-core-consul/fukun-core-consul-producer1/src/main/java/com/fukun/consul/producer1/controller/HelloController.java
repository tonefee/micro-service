package com.fukun.consul.producer1.controller;

import com.fukun.producer.api.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 *
 * @author tangyifei
 * @since 2019-6-4 14:47:08
 * @since JDK1.8
 */
@RestController
public class HelloController implements HelloService {

    @Override
    @RequestMapping("/hello")
    public String hello() {
        return "hello consul 1";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "health";
    }

    @GetMapping("/foo")
    public String foo(String foo) {
        return "hello " + foo + "1";
    }

    @GetMapping("/timeout")
    public String timeout() {
        try {
            //睡5秒，网关Hystrix3秒超时，会触发熔断降级操作
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "timeout1";
    }
}
