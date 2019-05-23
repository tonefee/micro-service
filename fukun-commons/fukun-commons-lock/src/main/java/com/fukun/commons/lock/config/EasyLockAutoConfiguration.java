package com.fukun.commons.lock.config;

import com.fukun.commons.lock.handler.EasyLockAspectHandler;
import com.fukun.commons.lock.helper.BusinessKeyHelper;
import com.fukun.commons.lock.helper.LockInfoHelper;
import com.fukun.commons.lock.service.impl.LockFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ClassUtils;

/**
 * 自动配置类
 * 备注：redisson配置方法：https://github.com/redisson/redisson/wiki/
 *
 * @author tangyifei
 * @since 2019-5-23 15:25:31 PM
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedissonProperties.class)
@Import({EasyLockAspectHandler.class})
public class EasyLockAutoConfiguration {

    @Autowired
    private RedissonProperties redissonProperties;

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() throws Exception {
        Config config = new Config();
        if (redissonProperties.getClusterServer() != null) {
            config.useClusterServers().setPassword(redissonProperties.getPassword())
                    .addNodeAddress(redissonProperties.getClusterServer().getNodeAddresses());
        } else {
            config.useSingleServer().setAddress(redissonProperties.getAddress())
                    .setDatabase(redissonProperties.getDatabase())
                    .setPassword(redissonProperties.getPassword());
        }
        Codec codec = (Codec) ClassUtils.forName(redissonProperties.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

    @Bean
    public LockInfoHelper lockInfoProvider() {
        return new LockInfoHelper();
    }

    @Bean
    public BusinessKeyHelper businessKeyProvider() {
        return new BusinessKeyHelper();
    }

    @Bean
    public LockFactory lockFactory() {
        return new LockFactory();
    }
}
