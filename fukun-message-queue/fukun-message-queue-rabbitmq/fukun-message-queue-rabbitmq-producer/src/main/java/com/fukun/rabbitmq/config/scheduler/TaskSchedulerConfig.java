package com.fukun.rabbitmq.config.scheduler;

import com.fukun.rabbitmq.config.threadpool.TreadPoolConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定时任务配置类
 *
 * @author tangyifei
 * @date 2019年7月5日16:09:53
 */
@Configuration
@EnableScheduling
@AutoConfigureAfter(TreadPoolConfig.class)
public class TaskSchedulerConfig implements SchedulingConfigurer {

    @Resource(name = "scheduleThreadPool")
    private ExecutorService scheduleThreadPool;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduleThreadPool);
    }

//    @Bean(destroyMethod = "shutdown")
//    public Executor taskScheduler() {
//        return Executors.newScheduledThreadPool(100);
//    }
}
