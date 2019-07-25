package com.fukun.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 实现通过binlog日志异步实现数据同步，同步到es中
 *
 * @author tangyifei
 * @since 2019年7月12日16:00:20
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DataSynToEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataSynToEsApplication.class, args);
    }

}
