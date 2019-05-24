package com.fukun.demo.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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

    private String host;

    private String password;

    private Integer timeout;

    private Integer port;

    @Autowired
    private Pool pool;

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(jedisPoolConfig(), host, port, timeout, password);
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(pool.getMaxActive());
        jedisPoolConfig.setMaxIdle(pool.getMaxIdle());
        jedisPoolConfig.setMaxWaitMillis(pool.getMaxWait());
        return jedisPoolConfig;
    }

    @Configuration
    @ConfigurationProperties(prefix = "spring.redis.pool")
    @Getter
    @Setter
    class Pool {

        private Integer maxIdle;

        private Integer minIdle;

        private Integer maxActive;

        private Integer maxWait;

        private Boolean testOnBorrow;

        private Boolean testOnReturn;

        private Boolean testWhileIdle;

    }

}
