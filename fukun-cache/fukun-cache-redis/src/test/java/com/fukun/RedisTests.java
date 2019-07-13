package com.fukun;

import com.fukun.redis.RedisApplication;
import com.fukun.redis.config.redis.RedisHandler;
import com.fukun.redis.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存各种数据类型操作相关的单元测试
 *
 * @author tangyifei
 * @date 2019年7月5日17:13:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedisApplication.class})
public class RedisTests {

    @Resource
    private RedisHandler redisHandler;

    // ==============================针对字符串的操作============================

    /**
     * 普通缓存放入
     */
    @Test
    public void testOrdinaryCache() {
//        redisHandler.set("fukun:country:", "中国");
        User user = new User("张三", 25, "男");
        redisHandler.set("fukun:user:", user);
    }

    /**
     * 普通缓存放入，并设置超时时间
     */
    @Test
    public void testOrdinaryTimeoutCache() {
        redisHandler.set("fukun:country:", "美国", 10);
    }

    /**
     * 只有在 key 不存在时，普通缓存放入并设置时间
     */
    @Test
    public void testOrdinaryTimeoutCacheIfAbsent() {
        redisHandler.setIfAbsent("fukun:country:", "英国", 10);
    }

    /**
     * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始
     */
    @Test
    public void testSetOffset() {
        redisHandler.set("fukun:note:", "abc");
        redisHandler.setOffset("fukun:note:", 1, 4);
        // 比如上面的"abc"，如果执行上面第二行的代码，就变成了"abc"d"
    }

    /**
     * 同时设置一个或多个 key-value 对
     */
    @Test
    public void testMultiSetKeyValues() {
        Map<String, Object> map = new HashMap<>(1 << 2);
        map.put("first-name", "一飞");
        map.put("last-name", "唐");
        redisHandler.multiSet(map);
    }

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
     */
    @Test
    public void testMultiSetKeyValuesIfAbsent() {
        Map<String, Object> map = new HashMap<>(1 << 2);
        map.put("first-name", "亦菲");
        map.put("last-name", "刘");
        redisHandler.multiSetIfAbsent(map);
    }

    /**
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾
     */
    @Test
    public void testAppend() {
        redisHandler.append("last-name", "邦");
    }

    /**
     * 获取所有(一个或多个)给定 key 的值
     */
    @Test
    public void testMultiGet() {
        List<Object> strs = redisHandler.multiGet(new String[]{"last-name", "first-name"});
        if (!CollectionUtils.isEmpty(strs)) {
            int size = strs.size();
            for(int i = 0; i < size; i++) {
                System.out.println(strs.get(i));
                System.out.println("+++++++++++++++++++++++++");
            }
        }
    }

}
