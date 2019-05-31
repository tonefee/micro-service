/*
package com.fukun.order.config.feign;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

*/
/**
 * 库存api调用
 * ribbon的重试机制和Feign的重试机制有冲突，所以源码中默认关闭Feign的重试机制
 * 默认重试5次（包含首次请求），FeignClient的默认超时时间为10s，不会开启重试机制，需要自定义配置。
 * @author tangyifei
 * @since 2019-5-30 20:16:07
 *//*

@Configuration
public class StockFeignConfig {

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1), 5);
    }

}
*/
