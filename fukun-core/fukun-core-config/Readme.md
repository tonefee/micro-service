随着线上项目变的日益庞大，每个项目都散落着各种配置文件，如果采用分布式的开发模式，需要的配置文件随着服务增加而不断增多。某一个基础服务信息变更，都会引起一系列的更新和重启，运维苦不堪言也容易出错。
配置中心便是解决此类问题的灵丹妙药。  

市面上开源的配置中心有很多，BAT每家都出过，360的QConf、淘宝的diamond、百度的disconf都是解决这类问题。国外也有很多开源的配置中心Apache Commons Configuration、owner、cfg4j等等。
这些开源的软件以及解决方案都很优秀，但是我最钟爱的却是Spring Cloud Config，因为它功能全面强大，可以无缝的和spring体系相结合，够方便够简单颜值高我喜欢。  

# Spring Cloud Config
在我们了解spring cloud config之前，我可以想想一个配置中心提供的核心功能应该有什么  

1、提供服务端和客户端支持    
2、集中管理各环境的配置文件    
3、配置文件修改之后，可以快速的生效  
4、可以进行版本管理  
5、支持大的并发查询  
6、支持各种语言  

Spring Cloud Config可以完美的支持以上所有的需求。  

Spring Cloud Config项目是一个解决分布式系统的配置管理方案。它包含了Client和Server两个部分，server提供配置文件的存储、
以接口的形式将配置文件的内容提供出去，client通过接口获取数据、并依据此数据初始化自己的应用。Spring cloud使用git或svn存放配置文件，
默认情况下使用git，我们先以git为例做一套示例。  

首先在github上面创建了一个文件夹config-repo用来存放配置文件，为了模拟生产环境，我们创建以下三个配置文件：  
fukun-config-dev.properties    
fukun-config-test.properties    
fukun-config-prod.properties   
每个配置文件中都写一个属性fukun.env,属性值分别是 dev/test/pro 。  
但是访问外国的GitHub网站由于网络的原因（网络连接超时）导致访问失败，所以我这里就使用我之前码云上的config-repo。  
如下： 
 
![服务发现](pictures/p1.png)  

下面我们开始配置server端   

## server 端
### 1、添加依赖 
 
```
 <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
 </dependency>
```
只需要加入spring-cloud-config-server包引用既可。  

### 2、配置文件 

``` 
spring:
  cloud:
    config:
      server:
        git:
          uri: https://gitee.com/tangyifei/micro-service-architecture.git    # 配置git仓库的地址
          search-paths: config-repo                             # git仓库地址下的相对地址，可以配置多个，用,分割。
          username:                                             # git仓库的账号
          password:                                             # git仓库的密码
```
Spring Cloud Config也提供本地存储配置的方式。我们只需要设置属性spring.profiles.active=native，Config Server会默认从应用的src/main/resource目录下检索配置文件。
也可以通过spring.cloud.config.server.native.searchLocations=file:E:/properties/属性来指定配置文件的位置。虽然Spring Cloud Config提供了这样的功能，
但是为了支持更好的管理内容和版本控制的功能，还是推荐使用git的方式。  

### 3、启动类  
启动类添加@EnableConfigServer，激活对配置中心的支持
  
```
package com.fukun.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 分布式配置中心服务端
 *
 * @author tangyifei
 * @since 2019年6月25日17:07:24
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class ConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }

}
```
到此server端相关配置已经完成  

### 4、测试  
首先我们先要测试server端是否可以读取到github上面的配置信息，直接访问：http://localhost:8889/tang-config/dev   
返回信息如下：  
```  
{
	"name": "tang-config",
	"profiles": ["dev"],
	"label": null,
	"version": "92e1c5d89bdc1e321475bb8193cf156342d78b19",
	"state": null,
	"propertySources": [{
		"name": "https://gitee.com/tangyifei/micro-service-architecture.git/config-repo/tang-config-dev.properties",
		"source": {
			"neo.hello": "hello tangyifei06",
			"spring.datasource.type": "com.alibaba.druid.pool.DruidDataSource",
			"spring.datasource.url": "jdbc:mysql://182.254.241.24:3306/micro-service-architecture?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true",
			"spring.datasource.username": "root",
			"spring.datasource.password": "##!zggc5055",
			"spring.datasource.driverClassName": "com.mysql.jdbc.Driver",
			"spring.datasource.initialSize": "5  ",
			"spring.datasource.minIdle": "5  ",
			"spring.datasource.maxActive": "20  ",
			"spring.datasource.maxWait": "60000  ",
			"spring.datasource.timeBetweenEvictionRunsMillis": "60000  ",
			"spring.datasource.minEvictableIdleTimeMillis": "300000  ",
			"spring.datasource.validationQuery": "SELECT 1 FROM DUAL ",
			"spring.datasource.testWhileIdle": "true ",
			"spring.datasource.testOnBorrow": "false ",
			"spring.datasource.testOnReturn": "false ",
			"spring.datasource.poolPreparedStatements": "true ",
			"spring.datasource.maxPoolPreparedStatementPerConnectionSize": "20",
			"spring.datasource.filters": "stat,wall,log4j2",
			"spring.datasource.connectionProperties": "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000 ",
			"authority.datasource.druid.first.url": "jdbc:mysql://182.254.241.24:3306/micro-service-architecture?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true",
			"authority.datasource.druid.first.username": "root",
			"authority.datasource.druid.first.password": "##!zggc5055",
			"authority.datasource.druid.second.url": "jdbc:mysql://localhost:3306/micro-service-architecture?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true",
			"authority.datasource.druid.second.username": "root",
			"authority.datasource.druid.second.password": "root",
			"authority.datasource.druid.initialSize": "10",
			"authority.datasource.druid.maxActive": "10",
			"authority.datasource.druid.minIdle": "10",
			"authority.datasource.druid.maxWait": "10",
			"authority.datasource.druid.poolPreparedStatements": "true",
			"authority.datasource.druid.maxPoolPreparedStatementPerConnectionSize": "20",
			"authority.datasource.druid.timeBetweenEvictionRunsMillis": "60000",
			"authority.datasource.druid.minEvictableIdleTimeMillis": "300000",
			"authority.datasource.druid.testWhileIdle": "true",
			"authority.datasource.druid.testOnBorrow": "false",
			"authority.datasource.druid.testOnReturn": "false",
			"authority.datasource.druid.statViewServlet.enabled": "true",
			"authority.datasource.druid.statViewServlet.url-pattern": "/druid/*",
			"authority.datasource.druid.filter.stat.log-slow-sql": "1000",
			"authority.datasource.druid.filter.stat.slowSqlMillis": "1000",
			"authority.datasource.druid.filter.stat.mergeSql": "false",
			"authority.datasource.druid.filter.wall.config.multi-statement-allow": "true",
			"mongodb.host": "115.159.201.120",
			"mongodb.port": "27017",
			"mongodb.db": "admin",
			"mongodb.username": "root",
			"mongodb.password": "##!zggc5055",
			"redis.hostName": "115.159.201.120",
			"redis.port": "6379",
			"redis.password": "##!zggc5055",
			"redis.timeout": "10000",
			"redis.maxIdle": "300",
			"redis.maxActive": "600",
			"redis.maxTotal": "1000",
			"redis.maxWaitMillis": "1000",
			"redis.minEvictableIdleTimeMillis": "300000",
			"redis.numTestsPerEvictionRun": "1024",
			"redis.timeBetweenEvictionRunsMillis": "30000",
			"redis.testOnBorrow": "true",
			"redis.testWhileIdle": "true"
		}
	}]
}
```
 




