package com.fukun.user.service.config.bean;

import com.fukun.commons.web.decoder.FeignErrorDecoder;
import com.fukun.commons.web.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局异常处理器和feign错误解码器相关的配置类
 *
 * @author tangyifei
 * @since 2019-5-24 09:47:05
 */
@Configuration
public class BeanConfig {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public FeignErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }

}
