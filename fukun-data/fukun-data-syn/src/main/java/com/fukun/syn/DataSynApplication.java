package com.fukun.syn;

import com.fukun.syn.canal.CanalClient;
import com.fukun.syn.initial.SpringContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 实现通过binlog日志异步实现数据同步
 *
 * @author tangyifei
 * @since 2019年7月12日16:00:20
 */
@SpringBootApplication
public class DataSynApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataSynApplication.class, args);
        CanalClient canalClient = SpringContext.getBean(CanalClient.class);
        canalClient.createConnect();
    }

}
