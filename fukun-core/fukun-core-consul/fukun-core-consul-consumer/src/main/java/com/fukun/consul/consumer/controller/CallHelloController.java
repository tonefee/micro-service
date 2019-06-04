package com.fukun.consul.consumer.controller;

import com.fukun.producer.api.HelloService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 验证负载均衡
 *
 * @author tangyifei
 * @since 2019-6-4 15:48:52
 * @since JDK1.8
 */
@RestController
public class CallHelloController {

//    private final static String SERVICE_ID = "consul-service-producer";
//
//    @Autowired
//    private LoadBalancerClient loadBalancer;

    @Resource
    private HelloService helloService;

//    @RequestMapping("/call")
//    public String call() {
//        ServiceInstance serviceInstance = loadBalancer.choose(SERVICE_ID);
//        System.out.println("服务地址：" + serviceInstance.getUri());
//        System.out.println("服务名称：" + serviceInstance.getServiceId());
//
//        String callServiceResult = new RestTemplate().getForObject(serviceInstance.getUri().toString() + "/hello", String.class);
//        System.out.println(callServiceResult);
//        return callServiceResult;
//
//    }

    @RequestMapping("/call")
    public String call() {
        return helloService.hello();
    }
}
