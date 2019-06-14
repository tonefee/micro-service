package com.fukun.producer.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 服务实现
 *
 * @author tangyifei
 * @since 2019-5-24 09:24:06
 * @since JDK1.8
 */
@FeignClient(value = "consul-service-producer")
public interface HelloService {

    /**
     * 负载均衡返回结果
     *
     * @return 结果
     */
    @RequestMapping("/hello")
    String hello();
}
