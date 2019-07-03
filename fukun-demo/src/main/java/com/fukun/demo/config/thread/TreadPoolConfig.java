package com.fukun.demo.config.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.*;

/**
 * 线程池配置
 *
 * @author tangyifei
 * @since 2019年7月3日11:23:33
 */
@Configuration
@EnableConfigurationProperties(TreadPoolConfig.TreadPoolProperties.class)
@AutoConfigureAfter(TreadPoolConfig.TreadPoolProperties.class)
public class TreadPoolConfig {

    @Resource
    private TreadPoolProperties treadPoolProperties;

    /**
     * 消费队列线程
     *
     * @return 线程池对象
     */
    @Bean(value = "consumerQueueThreadPool")
    public ExecutorService buildConsumerQueueThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("consumer-queue-thread-%d").build();

        ExecutorService pool = new ThreadPoolExecutor(treadPoolProperties.getCoreSize(), treadPoolProperties.getMaxSize(), treadPoolProperties.getActiveTime(), TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue(treadPoolProperties.getBlockQueueSize()), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        return pool;
    }

    /**
     * 读取线程池相关的属性的类
     *
     * @author tangyifei
     * @since 2019年7月3日11:33:11
     */
    @ConfigurationProperties(prefix = "thread.pool")
    @Getter
    @Setter
    class TreadPoolProperties {
        private Integer coreSize;
        private Integer maxSize;
        private Long activeTime;
        private Integer blockQueueSize;
    }

}
