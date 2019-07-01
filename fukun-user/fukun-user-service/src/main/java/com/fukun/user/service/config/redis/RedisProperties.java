package com.fukun.user.service.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * redis属性配置类
 *
 * @author tangyifei
 * @since 2019-5-24 10:06:24
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Getter
@Setter
public class RedisProperties {

    private Integer database;

    private String host;

    private String password;

    private Integer timeout;

    private Integer port;

    @Autowired
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
