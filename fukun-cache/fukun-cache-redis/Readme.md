# RedisTemplate 和 StringRedisTemplate 对比
RedisTemplate 看这个类的名字后缀是 Template，如果了解过 Spring 如何连接关系型数据库的，大概不会难猜出这个类是做什么的 ，
它跟 JdbcTemplate 一样，JdbcTemplate 封装了对数据库的常用操作，而 RedisTemplate 封装了对Redis的一些常用的操作，
当然 StringRedisTemplate 跟 RedisTemplate 功能类似那么肯定就会有人问，为什么会需要两个Template呢，
一个不就够了吗？其实他们两者之间的区别主要在于他们使用的序列化类。  
RedisTemplate 使用的是 JdkSerializationRedisSerializer 序列化对象，而且对象必须
实现序列化接口。  

## StringRedisTemplate  
主要用来存储字符串和序列化字符串，StringRedisSerializer 的泛型指定的是 String。当存入对象时，会报错 ：can not cast into String。   
可见性强，更易维护。如果都是通过字符串存储可考虑用 StringRedisTemplate。  

## RedisTemplate 
在微服务都是以HTTP接口的形式暴露自身服务的，因此在调用远程服务时就必须使用HTTP客户端。
我们可以使用JDK原生的URLConnection、Apache的Http Client、Netty的异步HTTP Client, Spring的RestTemplate。
这里介绍的是RestTemplate。RestTemplate底层用还是HttpClient，对其做了封装，使用起来更简单。   
RestTemplate是Spring提供的用于访问Rest服务的客户端，RestTemplate提供了多种便捷访问远程Http服务的方法,能够大大提高客户端的编写效率。 
调用RestTemplate的默认构造函数，RestTemplate对象在底层通过使用java.net包下的实现创建HTTP 请求，
可以通过使用ClientHttpRequestFactory指定不同的HTTP请求方式。  
ClientHttpRequestFactory接口主要提供了两种实现方式  
一种是SimpleClientHttpRequestFactory，使用J2SE提供的方式（既java.net包提供的方式）创建底层的Http请求连接。  
一种方式是使用HttpComponentsClientHttpRequestFactory方式，底层使用HttpClient访问远程的Http服务，使用HttpClient可以配置连接池和证书等信息。  
可以用来存储对象并序列化对象，但是要实现Serializable接口。 
其实spring并没有真正的去实现底层的http请求（3次握手），而是集成了别的http请求，spring只是在原有的各种http请求进行了规范标准，
让开发者更加简单易用，底层默认用的是jdk的http请求。     
以二进制数组方式存储，内容没有可读性。  

## RestTemplate的优缺点
优点：连接池、超时时间设置、支持异步、请求和响应的编解码。  
缺点：依赖别的spring版块、参数传递不灵活。  
RestTemplate默认是使用SimpleClientHttpRequestFactory，内部是调用jdk的HttpConnection，默认超时为-1。  

## RedisTemplate 序列化方式比较
那有没有办法，可以序列化对象，可读性又强呢？  
1、手动转化成json串再存储。取出数据需要反序列化。  
2、使用其他序列化方式。  

spring-data-redis 提供如下几种选择：  
GenericToStringSerializer: 可以将任何对象泛化为字符串并序列化  
Jackson2JsonRedisSerializer: 跟 JacksonJsonRedisSerializer 实际上是一样的  
JacksonJsonRedisSerializer: 序列化object对象为json字符串  
JdkSerializationRedisSerializer: 序列化java对象  
StringRedisSerializer: 简单的字符串序列化  

## 几种序列化方式的性能比较
经过使用 RedisSerialTests 单元测试后， 发现JdkSerializationRedisSerializer序列化后长度最小，Jackson2JsonRedisSerializer效率最高。  
如果综合考虑效率和可读性，牺牲部分空间，推荐key使用StringRedisSerializer，保持的key简明易读；value可以使用Jackson2JsonRedisSerializer。  
如果空间比较敏感，效率要求不高，推荐key使用StringRedisSerializer，保持的key简明易读；value可以使用JdkSerializationRedisSerializer。  
相关的配置请参考 RedisConfig 这个类。  






