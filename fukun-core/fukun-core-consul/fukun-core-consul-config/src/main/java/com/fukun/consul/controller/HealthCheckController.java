package com.fukun.consul.controller;

import com.fukun.consul.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 健康检查控制器
 *
 * @author tangyifei
 * @since 2019-6-4 14:47:08
 * @since JDK1.8
 */
@RestController
@Slf4j
public class HealthCheckController {

    @GetMapping("/health")
    public String healthCheck() {
        return "health";
    }

    @Resource
    private Config config;

    @GetMapping(value = "/config")
    public String getConfig() {
        if (log.isInfoEnabled()) {
            log.info("获取的测试值：{}", config.getTestValue());
        }
        return config.toString();
    }
}
