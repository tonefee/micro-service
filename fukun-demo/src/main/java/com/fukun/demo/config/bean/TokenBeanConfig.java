package com.fukun.demo.config.bean;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fukun.commons.enums.CacheKeyEnum;
import com.fukun.demo.config.redis.RedisConfig;
import com.fukun.user.model.bo.LoginToken;
import com.fukun.user.token.service.LoginTokenService;
import com.fukun.user.token.service.impl.LoginTokenCacheServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import javax.annotation.Resource;

/**
 * token相关的配置类
 *
 * @author tangyifei
 * @since 2019-5-24 13:55:53
 */
@Configuration
@ConditionalOnClass(value = {LoginTokenCacheServiceImpl.class, RedisTemplate.class})
@AutoConfigureAfter({RedisConfig.class})
public class TokenBeanConfig {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 注册配置登录token缓存相关的bean
     * 主要执行登录token相关的缓存操作
     *
     * @param loginTokenRedisTemplate 登录token redis缓存模板
     * @return 用户登录TOKEN服务
     */
    @Bean
    @ConditionalOnMissingBean(name = "loginTokenService")
    public LoginTokenService loginTokenService(RedisTemplate<String, LoginToken> loginTokenRedisTemplate) {
        return new LoginTokenCacheServiceImpl(loginTokenRedisTemplate, CacheKeyEnum.VALUE_LOGIN_TOKENS.code());
    }

    /**
     * 注册登录token相关的redis模板相关的bean
     *
     * @return 登录token相关的redis模板
     */
    @Bean
    public RedisTemplate<String, LoginToken> loginTokenRedisTemplate() {
        RedisTemplate<String, LoginToken> tokenRedisTemplate = new RedisTemplate<>();
        tokenRedisTemplate.setKeySerializer(new GenericToStringSerializer<>(String.class));
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Jackson2JsonRedisSerializer<LoginToken> serializer = new Jackson2JsonRedisSerializer<>(LoginToken.class);
        serializer.setObjectMapper(mapper);
        tokenRedisTemplate.setValueSerializer(serializer);
        tokenRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return tokenRedisTemplate;
    }
}
