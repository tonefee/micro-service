package com.fukun.commons.lock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redisson配置
 *
 * @author tangyifei
 * @since 2019-5-23 15:26:35 PM
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "easy.lock.redisson")
public class RedissonProperties {

    private String address;

    private String password;

    private int database = 15;

    private ClusterServer clusterServer;

    private String codec = "org.redisson.codec.JsonJacksonCodec";

    private long waitTime = 60;

    private long leaseTime = 60;

    @Setter
    @Getter
    public static class ClusterServer {

        private String[] nodeAddresses;

    }
}
