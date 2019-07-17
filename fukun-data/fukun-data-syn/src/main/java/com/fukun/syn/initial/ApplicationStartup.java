package com.fukun.syn.initial;

import com.fukun.syn.producer.BaseCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * spring boot容器启动并加载完后，开一些线程或者一些程序来干某些事情
 * 相关博客请查看 https://www.jianshu.com/p/01f7a971a4b9
 *
 * @author tangyifei
 * @date 2019年7月6日14:07:10
 */
@Slf4j
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //在容器加载完毕后获取配置文件中的配置，注意这里不能使用@Resource注解获取RabbitTemplate的这个bean，可能会报空指针
        RabbitTemplate rabbitTemplate = contextRefreshedEvent.getApplicationContext().getBean(RabbitTemplate.class);
        if (null != rabbitTemplate) {
            if (log.isInfoEnabled()) {
                log.info("rabbitMq模板类：{}", rabbitTemplate.toString());
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info("rabbitMq模板类没有初始化成功！");
            }
        }
        BaseCallBack.rabbitTemplate = rabbitTemplate;
    }
}
