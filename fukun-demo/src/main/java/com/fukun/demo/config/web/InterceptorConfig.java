package com.fukun.demo.config.web;

import com.fukun.commons.web.interceptor.HeaderParamsCheckInterceptor;
import com.fukun.commons.web.interceptor.LoginAuthInterceptor;
import com.fukun.commons.web.interceptor.ResponseResultInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author tangyifei
 * @since 2019-5-24 14:50:45
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 注册校验用户是否登录的拦截器相关的bean
     *
     * @return 拦截用户是否登录的拦截器
     */
    @Bean
    public LoginAuthInterceptor loginAuthInterceptor() {
        return new LoginAuthInterceptor();
    }

    /**
     * 注册接口响应体控制拦截器相关的bean
     *
     * @return 接口响应体控制拦截器对象
     */
    @Bean
    public ResponseResultInterceptor responseResultInterceptor() {
        return new ResponseResultInterceptor();
    }

    /**
     * 注册校验请求头中相关参数完整性和有效性拦截器相关的bean
     *
     * @return 校验请求头中相关参数完整性和有效性拦截器对象
     */
    @Bean
    public HeaderParamsCheckInterceptor headerParamsCheckInterceptor() {
        return new HeaderParamsCheckInterceptor();
    }

    /**
     * 注册拦截器
     *
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //响应结果控制拦截
        registry.addInterceptor(responseResultInterceptor());
        //登录拦截
        registry.addInterceptor(loginAuthInterceptor());
    }

}
