package com.fukun.demo.config.web;

import com.fukun.commons.web.resolver.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 参数解析配置类，用于权限校验和判断是否登录
 *
 * @author tangyifei
 * @since 2019-5-24 14:46:35
 */
@Configuration
public class ArgumentResolverConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginUserArgumentResolver());
    }

}
