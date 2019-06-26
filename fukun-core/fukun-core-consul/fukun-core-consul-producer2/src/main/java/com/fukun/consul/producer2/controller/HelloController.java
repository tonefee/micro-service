package com.fukun.consul.producer2.controller;

import com.fukun.producer.api.HelloService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试控制器
 *
 * @author tangyifei
 * @since 2019-6-4 14:47:08
 * @since JDK1.8
 */
@RestController
@RefreshScope
public class HelloController implements HelloService {

    AtomicInteger ac = new AtomicInteger();

    @Override
    @RequestMapping("/hello")
    public String hello() {
        return "hello consul 2";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "health";
    }

    @GetMapping("/foo")
    public String foo(String foo) {
        return "hello " + foo + "2";
    }

    @GetMapping("/timeout")
    public String timeout() {
        try {
            //睡5秒，网关Hystrix3秒超时，会触发熔断降级操作
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "timeout2";
    }

    @GetMapping("/retry")
    public String retry(@RequestParam(value = "name", required = false) String name) {
        System.err.println("第一次请求加上失败的重试次数：" + ac.addAndGet(1));
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("error");
        }
        return "retry2";
    }

    @PostMapping("/test")
    public String test() {
        return "test2";
    }

    @Value("${neo.hello}")
    private String hello;

    @RequestMapping("/config")
    public String getConfigDetail() {
        return this.hello;
    }
}
