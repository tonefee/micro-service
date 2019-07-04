# 业务场景
对于demno7中说到的线程池的使用可以节省系统的大量时间和系统开销。但是线程池也会带来新的问题，
如果我们很多业务都依赖于同一个线程池，当其中一个业务因为各种不可控的原因消耗了所有的线程，导致线程池全部占满。  
这样其他的业务也就不能正常运转了，这对系统的打击是巨大的。  
比如我们 Tomcat 接受请求的线程池，假设其中一些响应特别慢，线程资源得不到回收释放；线程池慢慢被占满，最坏的情况就是整个应用都不能提供服务。  
所以我们需要将线程池进行隔离。  
通常的做法是按照业务进行划分：  
`比如下单的任务用一个线程池，获取数据的任务用另一个线程池。这样即使其中一个出现问题把线程池耗尽，那也不会影响其他的任务运行。`   

# 使用hystrix实现线程池的隔离
这样的需求 Hystrix 已经帮我们实现了。  
Hystrix 是一款开源的容错插件，具有依赖隔离、系统容错降级等功能。  
首先需要定义两个线程池，分别用于执行订单、处理用户。  
```
package com.fukun.demo.web.demo8;

import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 定义订单服务的线程池
 *
 * @author tangyifei
 * @since 2019年7月4日09:53:02
 * @since jdk1.8
 */
@Slf4j
public class CommandOrder extends HystrixCommand<String> {

    private String orderName;

    public CommandOrder(String orderName) {

        super(Setter.withGroupKey(
                //服务分组
                HystrixCommandGroupKey.Factory.asKey("OrderGroup"))
                //线程分组
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("OrderPool"))
                //线程池配置
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(5)
                        .withKeepAliveTimeMinutes(5)
                        .withMaxQueueSize(10)
                        .withQueueSizeRejectionThreshold(10000))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
        )
        ;
        this.orderName = orderName;
    }

    @Override
    public String run() throws Exception {
        if (log.isInfoEnabled()) {
            log.info("orderName=[{}]", orderName);
        }
        TimeUnit.MILLISECONDS.sleep(100);
        return "OrderName=" + orderName;
    }
}
```
```
package com.fukun.demo.web.demo8;

import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 定义用户服务的线程池
 *
 * @author tangyifei
 * @since 2019年7月4日10:26:42
 * @since jdk1.8
 */
@Slf4j
public class CommandUser extends HystrixCommand<String> {
    private String userName;

    public CommandUser(String userName) {


        super(Setter.withGroupKey(
                //服务分组
                HystrixCommandGroupKey.Factory.asKey("UserGroup"))
                //线程分组
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("UserPool"))

                //线程池配置
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(10)
                        .withKeepAliveTimeMinutes(5)
                        .withMaxQueueSize(10)
                        .withQueueSizeRejectionThreshold(10000))

                //线程池隔离
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
        )
        ;
        this.userName = userName;
    }

    @Override
    public String run() throws Exception {
        if (log.isInfoEnabled()) {
            log.info("userName=[{}]", userName);
        }
        TimeUnit.MILLISECONDS.sleep(100);
        return "userName=" + userName;
    }

}
```
然后模拟运行：  
```
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
    public void execute(@RequestParam(value = "type", required = false) int type,
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
```
注意上面的CommandOrder或者CommandUser是原型模式，不能是单例模式，每次执行需要 new 一个实例，不然会报 This instance can only be executed once. Please instantiate a new instance. 异常。  

执行结果：  
```
2019-07-04 11:06:32,444 [hystrix-OrderPool-1] INFO (CommandOrder.java:44)- orderName=[手机]
2019-07-04 11:06:32,550 [http-nio-8723-exec-8] INFO (HystrixThreadPool.java:50)- execute=[OrderName=手机]
2019-07-04 11:06:32,566 [hystrix-UserPool-1] INFO (CommandUser.java:48)- userName=[刘亦菲]
2019-07-04 11:06:32,669 [http-nio-8723-exec-8] INFO (HystrixThreadPool.java:56)- name=[userName=刘亦菲]
```
可以看到两个任务分成了两个线程池运行，他们之间互不干扰。  
获取任务任务结果支持同步阻塞和异步非阻塞方式，可自行选择。  
它的实现原理其实容易猜到：利用一个 Map 来存放不同业务对应的线程池。   
通过刚才的构造函数也能证明。  

