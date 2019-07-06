//package com.fukun;
//
//import com.fukun.rabbitmq.ProducerApplication;
//import com.fukun.rabbitmq.model.Order;
//import com.fukun.rabbitmq.producer.RabbitOrderSender;
//import com.fukun.rabbitmq.service.OrderService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.util.UUID;
//
///**
// * 单元测试
// *
// * @author tangyifei
// * @date 2019年7月5日17:13:23
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {ProducerApplication.class})
//public class RabbitmqTests {
//
//    @Resource
//    private RabbitOrderSender rabbitOrderSender;
//
//    @Resource
//    private OrderService orderService;
//
//    /**
//     * 测试发送订单
//     */
//    @Test
//    public void testSender2() {
//        Order order = new Order();
//        order.setId("2018080400000001");
//        order.setName("测试订单");
//        order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());
//        rabbitOrderSender.sendOrder(order);
//    }
//
//    /**
//     * 测试发送订单并且入库（业务库和消息记录库）
//     */
//    @Test
//    public void testCreateOrder() {
//        Order order = new Order();
//        order.setId(UUID.randomUUID().toString());
//        order.setName("测试创建订单");
//        order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());
//        orderService.createOrder(order);
//    }
//}
