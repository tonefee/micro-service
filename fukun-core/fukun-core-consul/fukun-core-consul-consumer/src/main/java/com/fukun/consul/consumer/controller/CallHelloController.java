package com.fukun.consul.consumer.controller;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.Check;
import com.fukun.producer.api.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;

/**
 * 验证负载均衡
 *
 * @author tangyifei
 * @since 2019-6-4 15:48:52
 * @since JDK1.8
 */
@RestController
@Slf4j
public class CallHelloController {

//    private final static String SERVICE_ID = "consul-service-producer";
//
//    @Autowired
//    private LoadBalancerClient loadBalancer;

    @Resource
    private HelloService helloService;

    @Autowired
    private ConsulClient consulClient;

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

    /**
     * 剔除所有无效的服务实例
     */
    @GetMapping(value = "/clear-invalid-services")
    public String CheakCleanService() {
        log.info("***********************consul上无效服务清理开始*******************************************");
        // 获取所有的services检查信息
        Iterator<Map.Entry<String, Check>> it = consulClient.getAgentChecks().getValue().entrySet().iterator();
        Map.Entry<String, Check> serviceMap;
        while (it.hasNext()) {
            //迭代数据
            serviceMap = it.next();
            //获取服务名称
            String serviceName = serviceMap.getValue().getServiceName();
            //获取服务ID
            String serviceId = serviceMap.getValue().getServiceId();
            log.info("服务名称 :{}**服务ID:{}", serviceName, serviceId);
            //获取健康状态值  PASSING：正常  WARNING  CRITICAL  UNKNOWN：不正常
            log.info("服务 :{}的健康状态值：{}", serviceName, serviceMap.getValue().getStatus());
            if (serviceMap.getValue().getStatus() == Check.CheckStatus.CRITICAL) {
                log.info("服务 :{}为无效服务，准备清理...................", serviceName);
                consulClient.agentServiceDeregister(serviceId);
            }
        }
        return "clear invalid service finished";
    }

}
