package com.fukun.zuul.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查控制器
 *
 * @author tangyifei
 * @since 2019-6-4 14:47:08
 * @since JDK1.8
 */
@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public String healthCheck() {
        return "health";
    }
}
