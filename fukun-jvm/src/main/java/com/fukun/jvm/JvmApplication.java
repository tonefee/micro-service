package com.fukun.jvm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * jvm 相关的启动类
 *
 * @author tangyifei
 * @since 2019年7月15日17:31:02
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JvmApplication {

    public static void main(String[] args) {
        SpringApplication.run(JvmApplication.class, args);
    }

}
