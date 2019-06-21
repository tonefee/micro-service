//package com.fukun.gateway.config;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.interceptor.CacheErrorHandler;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisPassword;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import redis.clients.jedis.JedisPoolConfig;
//
//import javax.annotation.Resource;
//
///**
// * redis配置类
// * 关于@EnableCaching这个注解必须加，使配置生效
// *
// * @author tangyifei
// * @since 2019年6月21日15:08:18
// */
//@Configuration
//@EnableCaching
//@Slf4j
//public class RedisConfiguration extends CachingConfigurerSupport {
//
//    /**
//     * jedis连接工厂
//     */
//    @Resource
//    private JedisConnectionFactory jedisConnectionFactory;
//
//    @Bean
//    @Override
//    public KeyGenerator keyGenerator() {
//        // 设置自动key的生成规则，配置spring boot的注解，进行方法级别的缓存
//        // 使用：进行分割，可以很多显示出层级关系
//        // 这里其实就是new了一个KeyGenerator对象，只是这是lambda表达式的写法，我感觉很好用，大家感兴趣可以去了解下
//        return (target, method, params) -> {
//            StringBuilder sb = new StringBuilder();
//            sb.append(target.getClass().getName());
//            sb.append(":");
//            sb.append(method.getName());
//            for(Object obj : params) {
//                sb.append(":" + String.valueOf(obj));
//            }
//            String rsToUse = String.valueOf(sb);
//            log.info("自动生成Redis Key -> [{}]", rsToUse);
//            return rsToUse;
//        };
//    }
//
//    @Bean
//    @Override
//    public CacheManager cacheManager() {
//        // 初始化缓存管理器，在这里我们可以缓存的整体过期时间什么的，我这里默认没有配置
//        log.info("初始化 -> [{}]", "CacheManager RedisCacheManager Start");
//        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
//                .RedisCacheManagerBuilder
//                .fromConnectionFactory(jedisConnectionFactory);
//        return builder.build();
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
//        // 设置序列化
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        // 配置redisTemplate
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory);
//        RedisSerializer stringSerializer = new StringRedisSerializer();
//        // key序列化
//        redisTemplate.setKeySerializer(stringSerializer);
//        // value序列化
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        // Hash key序列化
//        redisTemplate.setHashKeySerializer(stringSerializer);
//        // Hash value序列化
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
//
//    @Override
//    @Bean
//    public CacheErrorHandler errorHandler() {
//        // 异常处理，当Redis发生异常时，打印日志，但是程序正常走
//        log.info("初始化 -> [{}]", "Redis CacheErrorHandler");
//        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
//            @Override
//            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
//                log.error("Redis occur handleCacheGetError：key -> [{}]", key, e);
//            }
//
//            @Override
//            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
//                log.error("Redis occur handleCachePutError：key -> [{}]；value -> [{}]", key, value, e);
//            }
//
//            @Override
//            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
//                log.error("Redis occur handleCacheEvictError：key -> [{}]", key, e);
//            }
//
//            @Override
//            public void handleCacheClearError(RuntimeException e, Cache cache) {
//                log.error("Redis occur handleCacheClearError：", e);
//            }
//        };
//        return cacheErrorHandler;
//    }
//
//    /**
//     * 此内部类就是把yml的配置数据，进行读取，创建JedisConnectionFactory和JedisPool，以供外部类初始化缓存管理器使用
//     * 不了解的同学可以去看@ConfigurationProperties和@Value的作用
//     *
//     * @author tangyifei
//     * @since 2019年6月21日15:16:18
//     */
//    @ConfigurationProperties
//    class DataJedisProperties {
//
//        @Value("${spring.redis.database}")
//        private int database;
//        @Value("${spring.redis.host}")
//        private String host;
//        @Value("${spring.redis.password}")
//        private String password;
//        @Value("${spring.redis.port}")
//        private int port;
//        @Value("${spring.redis.timeout}")
//        private int timeout;
//        @Value("${spring.redis.jedis.pool.max-idle}")
//        private int maxIdle;
//        @Value("${spring.redis.jedis.pool.min-idle}")
//        private int minIdle;
//        @Value("${spring.redis.jedis.pool.max-wait}")
//        private long maxWaitMillis;
//
//        @Bean
//        JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
//            log.info("Create JedisConnectionFactory successful");
//            // 单机版jedis
//            RedisStandaloneConfiguration redisStandaloneConfiguration =
//                    new RedisStandaloneConfiguration();
//            //设置redis服务器的host或者ip地址
//            redisStandaloneConfiguration.setHostName(host);
//            //设置默认使用的数据库
//            redisStandaloneConfiguration.setDatabase(database);
//            //设置密码
//            redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
//            //设置redis的服务的端口号
//            redisStandaloneConfiguration.setPort(port);
//            //获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢)
//            JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
//                    (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
//            //指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！）
//            jpcb.poolConfig(jedisPoolConfig);
//            //通过构造器来构造jedis客户端配置
//            JedisClientConfiguration jedisClientConfiguration = jpcb.build();
//            //单机配置 + 客户端配置 = jedis连接工厂
//            return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
//        }
//
//        @Bean
//        public JedisPoolConfig jedisPoolConfig() {
//            log.info("JedisPool init successful，host -> [{}]；port -> [{}]", host, port);
//            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//            jedisPoolConfig.setMaxIdle(maxIdle);
//            jedisPoolConfig.setMinIdle(minIdle);
//            jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
//            return jedisPoolConfig;
//        }
//    }
//
//}
