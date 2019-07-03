package com.fukun.demo.web.demo7;

import com.fukun.commons.web.annotations.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * 线程池的测试
 *
 * @author tangyifei
 * @since 2019-7-3 13:51:12
 */
@Slf4j
@ResponseResult
@RestController("ThreadPoolController")
@RequestMapping("thread/pool")
public class TreadPool {

    @Resource(name = "consumerQueueThreadPool")
    private ExecutorService consumerQueueThreadPool;

    @PostMapping
    public void execute(@RequestParam(value = "id", required = false) String id) {
        // 从线程池中获取线程并执行阻塞队列中的任务
        for(int i = 0; i < 5; i++) {
            consumerQueueThreadPool.execute(() -> {
                if (log.isInfoEnabled()) {
                    log.info("当前执行的线程的名称：{}，正在执行......", Thread.currentThread().getName());
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
