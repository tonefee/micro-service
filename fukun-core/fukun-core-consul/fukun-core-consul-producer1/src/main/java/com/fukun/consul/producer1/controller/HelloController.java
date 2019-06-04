package com.fukun.consul.producer1.controller;

import com.fukun.producer.api.HelloService;
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
}
