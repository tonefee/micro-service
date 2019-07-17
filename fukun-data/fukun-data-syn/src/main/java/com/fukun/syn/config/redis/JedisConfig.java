package com.fukun.syn.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * jedis相关的配置
 *
 * @author tangyifei
 * @since 2019-5-24 14:15:53
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Getter
@Setter
public class JedisConfig {

    private Integer database;

    private String host;

    private String password;

    private Integer timeout;

    private Integer port;

    @Resource
    private Pool pool;

    @Configuration
    @ConfigurationProperties(prefix = "spring.redis.jedis.pool")
    @Getter
    @Setter
    class Pool {

        private Integer maxIdle;

        private Integer minIdle;

        private Integer maxActive;

        private Integer maxWait;

        private Boolean testOnCreate;

        /**
         * 在获取连接的时候检查有效性
         */
        private Boolean testOnBorrow;

        /**
         * 当调用return Object方法时，是否进行有效性检查
         */
        private Boolean testOnReturn;

        /**
         * 在空闲时检查有效性
         */
        private Boolean testWhileIdle;

    }

}
