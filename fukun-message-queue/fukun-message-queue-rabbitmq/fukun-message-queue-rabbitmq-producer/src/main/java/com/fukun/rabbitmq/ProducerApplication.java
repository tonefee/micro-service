package com.fukun.rabbitmq;

import com.fukun.rabbitmq.initial.ApplicationStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 消息生产端
 *
 * @author tangyifei
 * @since 2019年7月5日14:01:59
 */
@SpringBootApplication
@EnableScheduling
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ProducerApplication.class);
        springApplication.addListeners(new ApplicationStartup());
        springApplication.run(args);
    }

}
