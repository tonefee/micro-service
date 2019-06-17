package com.fukun.consul.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 *
 * @author tangyifei
 * @since 2019-6-4 14:47:08
 * @since JDK1.8
 */
@RestController
public class ServiceController {

    private final static String SERVICE_ID = "consul-service-producer";

    /**
     * 通过LoadBalancerClient的choose(service-id)方法随机选择一个对应的应用名称的服务实例,
     * 然后使用 RestTemplate()来模拟发送请求。
     */
    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获取所有服务
     */
    @RequestMapping("/services")
    public Object services() {
        // 查询服务名称的所有实例信息
        return discoveryClient.getInstances(SERVICE_ID);
    }

    /**
     * 从所有服务中选择一个服务（轮询）
     */
    @RequestMapping("/discover")
    public Object discover() {
        // 随机选择一个服务名称对应的实例返回
        return loadBalancer.choose(SERVICE_ID).getUri().toString();
    }

}
