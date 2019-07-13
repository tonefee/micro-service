package com.fukun;

import com.fukun.redis.RedisApplication;
import com.fukun.redis.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存各种序列化方式的性能测试
 *
 * @author tangyifei
 * @date 2019年7月13日10:21:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedisApplication.class})
public class RedisSerialTests {

    @Test
    public void testSerial() {
        User user = new User("张三", 25, "男");
        List<Object> list = new ArrayList<>(200);
        for(int i = 0; i < 200; i++) {
            list.add(user);
        }

        // 序列化对象
        JdkSerializationRedisSerializer j = new JdkSerializationRedisSerializer();
        // 对象序列化成json
        GenericJackson2JsonRedisSerializer g = new GenericJackson2JsonRedisSerializer();
        // 对象序列化成json
        Jackson2JsonRedisSerializer j2 = new Jackson2JsonRedisSerializer(List.class);

        Long j_s_start = System.currentTimeMillis();
        // 通过JdkSerializationRedisSerializer进行序列化列表对象
        byte[] bytesJ = j.serialize(list);
        System.out.println("JdkSerializationRedisSerializer序列化时间：" + (System.currentTimeMillis() - j_s_start) + "ms,序列化后的长度：" + bytesJ.length);
        Long j_d_start = System.currentTimeMillis();
        j.deserialize(bytesJ);
        System.out.println("JdkSerializationRedisSerializer反序列化时间：" + (System.currentTimeMillis() - j_d_start));


        Long g_s_start = System.currentTimeMillis();
        byte[] bytesG = g.serialize(list);
        System.out.println("GenericJackson2JsonRedisSerializer序列化时间：" + (System.currentTimeMillis() - g_s_start) + "ms,序列化后的长度：" + bytesG.length);
        Long g_d_start = System.currentTimeMillis();
        g.deserialize(bytesG);
        System.out.println("GenericJackson2JsonRedisSerializer反序列化时间：" + (System.currentTimeMillis() - g_d_start));

        Long j2_s_start = System.currentTimeMillis();
        byte[] bytesJ2 = j2.serialize(list);
        System.out.println("Jackson2JsonRedisSerializer序列化时间：" + (System.currentTimeMillis() - j2_s_start) + "ms,序列化后的长度：" + bytesJ2.length);
        Long j2_d_start = System.currentTimeMillis();
        j2.deserialize(bytesJ2);
        System.out.println("Jackson2JsonRedisSerializer反序列化时间：" + (System.currentTimeMillis() - j2_d_start));
        // 下面未执行结果
//        JdkSerializationRedisSerializer序列化时间：25ms,序列化后的长度：1267
//        JdkSerializationRedisSerializer反序列化时间：25
//        GenericJackson2JsonRedisSerializer序列化时间：142ms,序列化后的长度：17825
//        GenericJackson2JsonRedisSerializer反序列化时间：181
//        Jackson2JsonRedisSerializer序列化时间：3ms,序列化后的长度：10201
//        Jackson2JsonRedisSerializer反序列化时间：4
//        可以看出 Jackson2JsonRedisSerializer 序列化与反序列化的效率高，但是 JdkSerializationRedisSerializer 序列化后长度最小。

    }
}
