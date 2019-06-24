package com.fukun.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * 身份认证的局部过滤器
 * 可以继承 AbstractGatewayFilterFactory 或实现 GlobalFilter 实现过滤请求功能
 * GatewayFilter 只能指定路径上应用，GlobalFilter 可以在全局应用
 *
 * @author tangyifei
 * @since 2019年6月24日14:24:05
 */
@Slf4j
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {

    public AuthGatewayFilterFactory() {
        super(Config.class);
    }

    /**
     * 执行验证逻辑
     *
     * @param config
     * @return
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (log.isInfoEnabled()) {
                log.info("开始执行验证");
            }
            String token = exchange.getRequest().getHeaders().getFirst("token");
            if (Config.secret.equals(token)) {
                return chain.filter(exchange);
            }
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //设置headers
            HttpHeaders httpHeaders = response.getHeaders();
            httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
            httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            //设置body
            String warningStr = "未登录或登录超时";
            DataBuffer bodyDataBuffer = response.bufferFactory().wrap(warningStr.getBytes());
            return response.writeWith(Mono.just(bodyDataBuffer));
        };
    }

    static class Config {
        // TODO 操作人：唐一菲；  事由：优化后从redis中获取；时间：2019年6月24日14:26:51
        /**
         * 自定义一个服务端的token
         */
        static String secret = "1234";
    }

}
