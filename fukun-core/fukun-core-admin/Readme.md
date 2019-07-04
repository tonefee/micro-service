# SBA简介
SBA 全称 Spring Boot Admin 是一个管理和监控 Spring Boot 应用程序的开源项目。分为admin-server 与 admin-client 两个组件，
admin-server通过采集 actuator 端点数据，显示在 spring-boot-admin-ui 上，已知的端点几乎都有进行采集，
通过 spring-boot-admin 可以动态切换日志级别、导出日志、导出heapdump、监控各项指标 等等。  
Spring Boot Admin 在对单一应用服务监控的同时也提供了集群监控方案，支持通过eureka、consul、zookeeper等注册中心的方式实现多服务监控与管理。  
