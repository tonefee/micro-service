package com.fukun.demo.config.bean;

import com.fukun.commons.attributes.mapper.AttributeMapper;
import com.fukun.commons.attributes.service.AttributeService;
import com.fukun.commons.attributes.service.impl.AttributeServiceImpl;
import com.fukun.commons.web.aspect.RestControllerAspect;
import com.fukun.commons.web.decoder.FeignErrorDecoder;
import com.fukun.commons.web.handler.GlobalExceptionHandler;
import com.fukun.commons.web.handler.ResponseResultHandler;
import com.fukun.user.token.service.impl.LoginTokenCacheServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * bean相关的配置类
 *
 * @author tangyifei
 * @since 2019-5-24 11:54:20
 */
@Configuration
@ConditionalOnClass(value = {LoginTokenCacheServiceImpl.class, RedisTemplate.class})
public class BeanConfig {

    @Resource
    private AttributeMapper<String> attributeDao;

    /**
     * 注册全局异常处理器到spring容器中
     *
     * @return 全局异常处理器相关的bean
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * 注册feign错误解码器器到spring容器中
     *
     * @return feign错误解码器器相关的bean
     */
    @Bean
    public FeignErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }

    /**
     * 注册记录日志的控制器切面到spring容器中
     *
     * @return 记录日志的控制器切面相关的bean
     */
    @Bean
    public RestControllerAspect restControllerAspect() {
        return new RestControllerAspect();
    }

    /**
     * 注册接口响应体处理器到spring容器中
     *
     * @return 接口响应体处理器相关的bean
     */
    @Bean
    public ResponseResultHandler responseResultHandler() {
        return new ResponseResultHandler();
    }

    /**
     * 注册通用属性服务到spring容器中
     *
     * @return 通用属性服务相关的bean
     */
    @Bean
    public AttributeService<String> userAttributeService() {
        AttributeService<String> userAttributeService = new AttributeServiceImpl<>("user_attr", attributeDao, null);
        return userAttributeService;
    }
}
