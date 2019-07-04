package com.fukun.demo.web.demo8;

import com.fukun.commons.web.annotations.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 根据不同个业务实现线程池的隔离
 *
 * @author tangyifei
 * @since 2019年7月4日10:32:43
 * @since jdk1.8
 */
@Slf4j
@ResponseResult
@RestController("HystrixThreadPoolController")
@RequestMapping("hystrix/thread/pool")
public class HystrixThreadPool {

    /**
     * 使用不同业务相关的线程池执行相关的任务
     *
     * @param type      线程任务的执行方式：1表示同步阻塞，非1表示异步非阻塞
     * @param orderName 订单名称
     * @param userName  用户名称
     * @throws Exception 异常
     */
    @PostMapping
    public void execute(@RequestParam(value = "type", required = false, defaultValue = "0") int type,
                        @RequestParam(value = "orderName", required = false) String orderName,
                        @RequestParam(value = "userName", required = false) String userName) throws Exception {

        CommandOrder command = new CommandOrder(orderName);
        String execute;
        if (1 == type) {
            //同步阻塞方式执行
            execute = command.execute();
        } else {
            //异步非阻塞方式
            Future<String> queue = command.queue();
            execute = queue.get(200, TimeUnit.MILLISECONDS);
        }
        if (log.isInfoEnabled()) {
            log.info("execute=[{}]", execute);
        }

        CommandUser commandUser = new CommandUser(userName);
        String name = commandUser.execute();
        if (log.isInfoEnabled()) {
            log.info("name=[{}]", name);
        }
    }

}
