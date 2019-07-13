package com.fukun.redis.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * redis相关的配置
 *
 * @author tangyifei
 * @since 2019-5-24 14:18:17
 */
@Configuration
@EnableCaching
@AutoConfigureAfter({JedisConfig.class})
@Slf4j
public class RedisConfig extends CachingConfigurerSupport {

    @Resource
    private JedisConfig jedisConfig;

    /**
     * 定义Jedis相关的池配置
     *
     * @return jedis池配置对象
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        if (log.isInfoEnabled()) {
            log.info("创建Jedis池配置开始！");
        }
        JedisConfig.Pool pool = jedisConfig.getPool();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(pool.getMaxActive());
        jedisPoolConfig.setMaxIdle(pool.getMaxIdle());
        jedisPoolConfig.setMaxWaitMillis(pool.getMaxWait());
        jedisPoolConfig.setTestOnCreate(pool.getTestOnCreate());
        jedisPoolConfig.setTestOnBorrow(pool.getTestOnBorrow());
        jedisPoolConfig.setTestOnReturn(pool.getTestOnReturn());
        jedisPoolConfig.setTestWhileIdle(pool.getTestWhileIdle());
        if (log.isInfoEnabled()) {
            log.info("创建Jedis池配置结束！");
        }
        return jedisPoolConfig;
    }

    /**
     * redis连接工厂配置
     *
     * @return redis连接工厂配置对象
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(@Qualifier("jedisPoolConfig") JedisPoolConfig jedisPoolConfig) {
        if (log.isInfoEnabled()) {
            log.info("创建Jedis连接工厂开始！");
        }
        // 单机版jedis
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration();
        //设置redis服务器的host或者ip地址
        redisStandaloneConfiguration.setHostName(jedisConfig.getHost());
        //设置默认使用的数据库
        redisStandaloneConfiguration.setDatabase(jedisConfig.getDatabase());
        //设置密码
        redisStandaloneConfiguration.setPassword(RedisPassword.of(jedisConfig.getPassword()));
        //设置redis的服务的端口号
        redisStandaloneConfiguration.setPort(jedisConfig.getPort());
        //获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢)
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        //指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！）
        jpcb.poolConfig(jedisPoolConfig);
        //通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
        //单机配置 + 客户端配置 = jedis连接工厂
        if (log.isInfoEnabled()) {
            log.info("创建Jedis连接工厂结束！");
        }
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    /**
     * key生成策略
     *
     * @return key生成器对象
     */
    @Bean
    public KeyGenerator wiselyKeyGenerator() {

        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for(Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };

    }

    @Bean
    public CacheManager cacheManager(@Qualifier("redisConnectionFactory") RedisConnectionFactory factory) {
        RedisCacheManager cacheManager = RedisCacheManager.create(factory);
        return cacheManager;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        //设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //支持java8时间模块序列化
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * 针对字符串序列化方式，可以对key和value进行序列化
     *
     * @return 字符串redis序列化类
     */
    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    /**
     * 序列化object对象为json字符串，但使用时构造函数不用特定的类
     *
     * @return 序列化object对象为json字符串的序列化类
     */
    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper());
    }

    /**
     * 序列化object对象为json字符串
     *
     * @return 序列化object对象为json字符串的序列化类
     */
    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        //  new Jackson2JsonRedisSerializer(Object.class) 需要指明类型,
        //  例如：new Jackson2JsonRedisSerializer(User.class)，否则会报错：
        //  java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to com.example.demo.bean.User。
        // 或者开启默认类型：
        // ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper());
        return jackson2JsonRedisSerializer;
    }

    /**
     * 下面这个 RedisTemplate 可以用来存储对象，但是对象要实现Serializable接口
     *
     * @param factory redis 连接工厂
     * @return 封装相关的redis操作的模板类
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // 开启事务
        template.setEnableTransactionSupport(true);

        RedisSerializer<String> stringRedisSerializer = stringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = jackson2JsonRedisSerializer();

        //  key的序列化类型
        template.setKeySerializer(stringRedisSerializer);
        // 值的序列化类型
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //  hash的filed的序列化类型
        template.setHashKeySerializer(stringRedisSerializer);
        //  hash的value的序列化类型
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        // 不是通过spring注入方式使用RedisTemplate时，需先调用afterPropertiesSet()方法进行相关的初始化参数和初始工作
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 主要用来存储字符串，StringRedisSerializer的泛型指定的是String， 可见性强，更易维护
     * 如果都是字符串存储可考虑用StringRedisTemplate
     *
     * @param factory redis 连接工厂
     * @return 封装相关的redis操作的模板类
     */
    @Bean("stringRedisTemplate")
    public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        // 开启事务
        template.setEnableTransactionSupport(true);

        RedisSerializer<String> stringRedisSerializer = stringRedisSerializer();
        Jackson2JsonRedisSerializer<String> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(String.class);

        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisHandler redisHandler(RedisTemplate<String, Object> redisTemplate) {
        return new RedisHandler(redisTemplate);
    }

}
