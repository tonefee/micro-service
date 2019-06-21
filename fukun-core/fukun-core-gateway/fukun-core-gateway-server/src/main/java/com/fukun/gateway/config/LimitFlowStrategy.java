package com.fukun.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 配置相应的限流策略
 *
 * @author tangyifei
 * @since 2019年6月21日16:05:12
 */
@Configuration
public class LimitFlowStrategy {

    /**
     * 根据请求参数中的 foo字段来限流，也可以设置根据请求IP地址来限流
     * 这样网关就可以根据不同策略来对请求进行限流了。
     *
     * @return 限流策略
     */
    @Bean
    KeyResolver ipKeyResolver() {
        // 那么使劲刷新url，出现429 too many request 就表示成功了
        return exchange -> Mono.just(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getHostName());
//        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
//        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("foo"));
    }
}
