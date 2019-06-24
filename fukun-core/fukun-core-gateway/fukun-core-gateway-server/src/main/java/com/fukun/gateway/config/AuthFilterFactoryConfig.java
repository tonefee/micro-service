package com.fukun.gateway.config;

import com.fukun.gateway.filter.AuthGatewayFilterFactory;
import com.fukun.gateway.filter.AuthGlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义验证过滤器的配置类，注入AuthGatewayFilterFactory到spring容器中
 *
 * @author tangyifei
 * @since 2019年6月24日14:38:45
 */
@Configuration
public class AuthFilterFactoryConfig {

    /**
     * 创建自定义的局部验证过滤器工厂相关的bean
     *
     * @return 验证过滤器工厂相关的bean
     */
//    @Bean
//    public AuthGatewayFilterFactory authGatewayFilterFactory() {
//        return new AuthGatewayFilterFactory();
//    }

    /**
     * 创建自定义的全局验证过滤器工厂相关的bean
     *
     * @return 验证过滤器工厂相关的bean
     */
    @Bean
    public AuthGlobalFilter authGlobalFilter() {
        return new AuthGlobalFilter();
    }
}
