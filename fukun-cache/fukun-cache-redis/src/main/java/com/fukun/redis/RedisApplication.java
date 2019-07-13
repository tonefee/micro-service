package com.fukun.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

/**
 * 缓存模块 redis 相关的启动类
 *
 * @author tangyifei
 * @since 2019年7月12日16:00:20
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
