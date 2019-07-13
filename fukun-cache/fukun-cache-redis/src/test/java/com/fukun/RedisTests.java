package com.fukun;

import com.alibaba.fastjson.JSON;
import com.fukun.redis.RedisApplication;
import com.fukun.redis.config.redis.RedisHandler;
import com.fukun.redis.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 缓存各种数据类型操作相关的单元测试
 *
 * @author tangyifei
 * @date 2019年7月5日17:13:23
 */
@SpringBootTest(classes = {RedisApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisTests {

    @Resource
    private RedisHandler redisHandler;

    // ==============================针对字符串的操作============================

    /**
     * 普通缓存放入
     */
    @Test
    public void testOrdinaryCache() {
        redisHandler.set("fukun:country", "中国");
        User user = new User("张三", 25, "男");
        redisHandler.set("fukun:user", user);
    }

    /**
     * 普通缓存放入，并设置超时时间
     */
    @Test
    public void testOrdinaryTimeoutCache() {
        redisHandler.set("fukun:country", "美国", 10);
    }

    /**
     * 只有在 key 不存在时，普通缓存放入并设置时间
     */
    @Test
    public void testOrdinaryTimeoutCacheIfAbsent() {
        redisHandler.setIfAbsent("fukun:country", "英国", 10);
    }

    /**
     * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始
     */
    @Test
    public void testSetOffset() {
        redisHandler.set("fukun:note", "abc");
        redisHandler.setOffset("fukun:note", 1, 4);
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
        Map<String, Object> map = new HashMap<>(1 << 2);
        map.put("country1", "中国");
        map.put("country2", "美国");
        redisHandler.multiSetIfAbsent(map);
        List<Object> strs = redisHandler.multiGet(new String[]{"country1", "country2"});
        if (!CollectionUtils.isEmpty(strs)) {
            int size = strs.size();
            for(int i = 0; i < size; i++) {
                System.out.println(strs.get(i));
                System.out.println("+++++++++++++++++++++++++");
            }
        }
    }

    /**
     * 获取key的值并且转化为普通对象
     */
    @Test
    public void testGetObject() {
        User user = new User("张三", 25, "男");
        redisHandler.set("fukun:user", user);
        System.out.println(JSON.toJSON(redisHandler.get("fukun:user", User.class)));
    }

    /**
     * 获取key对应的某一个范围的字符串值
     */
    @Test
    public void testGetRange() {
        redisHandler.set("fukun:note", "abc");
        System.out.println(redisHandler.getRange("fukun:note", 1, 2));
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
     */
    @Test
    public void testGetSet() {
        redisHandler.set("fukun:note", "abc");
        System.out.println(redisHandler.getSet("fukun:note", "mns"));
    }

    /**
     * 返回 key 所储存的字符串值的长度
     */
    @Test
    public void testGetStrLen() {
        // 如果存储的是123456，长度是6
        redisHandler.set("fukun:note", 123456);
        // 如果存储的是字符串123456，如同下面的形式，那么长度是8
        // redisHandler.set("fukun:note", "123456");
        System.out.println("字符串值的长度：" + redisHandler.getStrLen("fukun:note") + " " + "值：" + redisHandler.get("fukun:note"));
    }

    // ==============================针对map的redis操作============================

    /**
     * HashSet同时将多个 field-value (域-值)对设置到哈希表 key 中，并设置超时时间
     */
    @Test
    public void testHmSet() {
        Map<String, Object> map = new HashMap<>(1 << 2);
        map.put("province", "江苏省");
        map.put("city", "南京市");
        redisHandler.hmset("fukun:address", map, 10);
    }

    /**
     * 将哈希表 key 中的字段 field 的值设为 value
     */
    @Test
    public void testHmSetField() {
        Map<String, Object> map = new HashMap<>(1 << 2);
        map.put("province", "江苏省");
        map.put("city", "南京市");
        redisHandler.hmset("fukun:address", map);
        redisHandler.hset("fukun:address", "city", "苏州市");
    }

    /**
     * 删除hash表中的值，删除一个或多个哈希表字段
     */
    @Test
    public void testHdel() {
        Map<String, Object> map = new HashMap<>(1 << 2);
        map.put("province", "江苏省");
        map.put("city", "南京市");
        redisHandler.hmset("fukun:address", map);
        redisHandler.hdel("fukun:address", new String[]{"province", "city"});
    }

    /**
     * 判断hash表中是否有该项的值，查看哈希表 key 中，指定的字段是否存在
     */
    @Test
    public void testHHasKey() {
        Map<String, Object> map = new HashMap<>(1 << 2);
        map.put("province", "江苏省");
        map.put("city", "南京市");
        redisHandler.hmset("fukun:address", map);
        System.out.println("hash表中某个key是否存在：" + redisHandler.hHasKey("fukun:address", "province"));
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     */
    @Test
    public void testHIncr() {
        System.out.println("递增的结果：" + redisHandler.hincr("fukun:user", "grade", 1));
    }

    /**
     * hash递减
     */
    @Test
    public void testHDecr() {
        System.out.println("递减的结果：" + redisHandler.hdecr("fukun:user", "grade", 1));
    }

    /**
     * 获取哈希表中所有值
     */
    @Test
    public void testHvalues() {
        Map<String, Object> map = new HashMap<>(1 << 2);
        map.put("province", "安徽省");
        map.put("city", "六安市");
        redisHandler.hmset("fukun:address", map);
        List<?> list = redisHandler.hvalues("fukun:address");
        if (!CollectionUtils.isEmpty(list)) {
            int size = list.size();
            for(int i = 0; i < size; i++) {
                System.out.println("获取hash表中的值：" + list.get(i));
                System.out.println("+++++++++++++++++++++++++++");
            }
        }
    }

    /**
     * 获取哈希表中字段的数量
     */
    @Test
    public void testHLen() {
        Map<String, Object> map = new HashMap<>(1 << 2);
        map.put("province", "安徽省");
        map.put("city", "合肥市");
        redisHandler.hmset("fukun:address", map);
        System.out.println("哈希表中字段的数量：" + redisHandler.hLen("fukun:address"));
    }

    //============================针对set集合的操作=============================

    /**
     * 向set集合中添加一个或多个成员
     */
    @Test
    public void testSAdd() {
        redisHandler.sAdd("fukun:sport", new String[]{"打篮球", "踢足球"});
    }

    /**
     * 根据value从一个set中查询,是否存在，即判断 member 元素是否是集合 key 的成员
     */
    @Test
    public void testSHasKey() {
        System.out.println("是不是某一个set集合中的成员：" + redisHandler.sHasKey("fukun:sport", "打篮球"));
    }

    /**
     * 根据key获取Set中的所有值，即返回集合中的所有成员
     */
    @Test
    public void testSGet() {
        redisHandler.sAdd("fukun:sport", new String[]{"跳绳", "游泳"});
        Set<Object> results = redisHandler.sGet("fukun:sport");
        if (!CollectionUtils.isEmpty(results)) {
            for(Object s : results) {
                System.out.println("从set集合中获取到的成员是：" + s);
                System.out.println("=============================");
            }

        }
    }

    /**
     * 返回给定所有集合的差集（比较多个key中的不同的值）
     */
    @Test
    public void testSDiff1() {
        redisHandler.sAdd("fukun:car", new String[]{"本田", "奥迪"});
        redisHandler.sAdd("fukun:bus", new String[]{"大众", "本田", "福特"});
        Set<Object> results = redisHandler.sDiff("fukun:car", "fukun:bus");
        if (!CollectionUtils.isEmpty(results)) {
            for(Object s : results) {
                System.out.println("两个set集合中的不相同的值是：" + s);
                System.out.println("=============================");
            }

        }
    }

    /**
     * 返回给定所有集合的差集，对多个key进行比较（比较多个key中的不同的值）
     */
    @Test
    public void testSDiff2() {
        redisHandler.sAdd("fukun:car", new String[]{"本田", "奥迪", "奔驰"});
        redisHandler.sAdd("fukun:bus", new String[]{"大众", "本田", "福特"});
        redisHandler.sAdd("fukun:bike", new String[]{"大众", "宝马", "滴滴"});
        Set<Object> results = redisHandler.sDiff("fukun:car", Arrays.asList(new String[]{"fukun:bus", "fukun:bike"}));
        if (!CollectionUtils.isEmpty(results)) {
            for(Object s : results) {
                System.out.println("多个set集合中的不相同的值是：" + s);
                System.out.println("=============================");
            }

        }
    }

    /**
     * 返回给定所有集合的差集并存储在 destination 中（比较多个key中的不同的值）
     */
    @Test
    public void testSDiffStore() {
        redisHandler.sAdd("fukun:car", new String[]{"本田", "奥迪", "奔驰"});
        redisHandler.sAdd("fukun:bus", new String[]{"大众", "本田", "福特"});
        redisHandler.sDiffStore("fukun:car", "fukun:bus", "fukun:diff");
    }

    /**
     * 返回给定所有集合的交集（比较多个key中的相同的值）
     */
    @Test
    public void testSInter1() {
        redisHandler.sAdd("fukun:car", new String[]{"本田", "奥迪"});
        redisHandler.sAdd("fukun:bus", new String[]{"大众", "本田", "福特"});
        Set<Object> results = redisHandler.sInter("fukun:car", "fukun:bus");
        if (!CollectionUtils.isEmpty(results)) {
            for(Object s : results) {
                System.out.println("两个set集合中的相同的值是：" + s);
                System.out.println("=============================");
            }

        }
    }

    /**
     * 返回给定所有集合的交集，对多个key进行比较（比较多个key中的相同的值）
     */
    @Test
    public void testSInter2() {
        redisHandler.sAdd("fukun:car", new String[]{"本田", "奥迪", "奔驰"});
        redisHandler.sAdd("fukun:bus", new String[]{"大众", "本田", "福特"});
        redisHandler.sAdd("fukun:bike", new String[]{"本田", "宝马", "滴滴"});
        Set<Object> results = redisHandler.sInter("fukun:car", Arrays.asList(new String[]{"fukun:bus", "fukun:bike"}));
        if (!CollectionUtils.isEmpty(results)) {
            for(Object s : results) {
                System.out.println("多个set集合中的相同的值是：" + s);
                System.out.println("=============================");
            }

        }
    }

    /**
     * 返回给定所有集合的交集并存储在 destination 中（比较多个key中的相同的值）
     */
    @Test
    public void testSInterStore() {
        redisHandler.sAdd("fukun:car", new String[]{"本田", "奥迪", "奔驰"});
        redisHandler.sAdd("fukun:bus", new String[]{"大众", "本田", "奔驰"});
        redisHandler.sInterStore("fukun:car", "fukun:bus", "fukun:same");
    }

    /**
     * 返回所有给定集合的并集（合并多个key中值）
     */
    @Test
    public void testSUnion() {
        redisHandler.sAdd("fukun:car", new String[]{"本田", "奥迪"});
        redisHandler.sAdd("fukun:bus", new String[]{"大众", "福特"});
        Set<Object> results = redisHandler.sUnion("fukun:car", "fukun:bus");
        if (!CollectionUtils.isEmpty(results)) {
            for(Object s : results) {
                System.out.println("两个set集合中的合并后的值是：" + s);
                System.out.println("=============================");
            }

        }
    }

    /**
     * 将 value 元素从 source 集合移动到 destination 集合
     */
    @Test
    public void testSMove() {
        redisHandler.sAdd("fukun:tiger", new String[]{"吃肉", "凶猛"});
        redisHandler.sAdd("fukun:lion", new String[]{"群居"});
        System.out.println("移动成功与否：" + redisHandler.sMove("fukun:tiger", "吃肉", "fukun:lion"));
    }

    /**
     * 返回集合中一个随机数但是不移除
     */
    @Test
    public void testSRandomMembers() {
        System.out.println("从set集合中返回的随机数是：" + redisHandler.sRandomMembers("fukun:lion"));
    }

    /**
     * 获取set集合的大小
     */
    @Test
    public void testSGetSetSize() {
        System.out.println("获取set集合的大小是：" + redisHandler.sGetSetSize("fukun:lion"));
    }

    /**
     * 移除集合中的元素
     */
    @Test
    public void testSRemove() {
        redisHandler.sRemove("fukun:lion", new String[]{"吃肉", "群居"});
    }

    //============================针对zset集合的操作=============================

    /**
     * 添加一个元素, zset与set最大的区别就是每个元素都有一个score，因此有个排序的辅助功能;  zadd
     * score越小，排序越靠前
     */
    @Test
    public void testZsetAdd() {
        redisHandler.zsetAdd("fukun:subject", "chinese", 71);
        redisHandler.zsetAdd("fukun:subject", "history", 72);
        redisHandler.zsetAdd("fukun:subject", "english", 74);
        redisHandler.zsetAdd("fukun:subject", "music", 73);
    }

    /**
     * 获取有序集合的成员数
     */
    @Test
    public void testZsetSize() {
        System.out.println("获取有序集合的成员数是：" + redisHandler.zsetSize("fukun:subject"));
    }

    /**
     * 计算在有序集合中指定区间分数的成员数
     */
    @Test
    public void testZsetCount() {
        System.out.println("获取成绩在70到73的成员数是：" + redisHandler.zsetCount("fukun:subject", 70, 73));
    }

    /**
     * 对分数进行增加
     */
    @Test
    public void testIncrScore1() {
        // 对历史成绩进行增加分数，比如当前历史分数为72，那么增加10后，历史成绩变为82
        redisHandler.incrScore("fukun:subject", "history", 10);
    }

    /**
     * 对分数进行减少
     */
    @Test
    public void testIncrScore2() {
        // 对历史成绩进行减少分数，比如当前历史分数为82，那么减少2后，历史成绩变为80
        redisHandler.incrScore("fukun:subject", "history", -2);
    }

    /**
     * 通过索引区间正序返回有序集合成指定区间内的成员，注意不是分数的区间，score小的在前面，分数由低到高
     */
    @Test
    public void testZgetRange() {
        // 下面是获取全部的，0 -1 表示获取全部的集合内容
        //Set<Object> results = redisHandler.zgetRange("fukun:subject", 0, -1);
        // 获取索引在0至2的科目，注意不是分数的区间
        Set<Object> results = redisHandler.zgetRange("fukun:subject", 0, 2);
        if (!CollectionUtils.isEmpty(results)) {
            for(Object s : results) {
                System.out.println("成绩在70至79的科目，分数由低到高：" + s);
                System.out.println("=============================");
            }

        }
    }

    /**
     * 通过索引区间倒序返回有序集合成指定区间内的成员，注意不是分数的区间，通过索引，分数从高到底，score大的在前面
     */
    @Test
    public void testZsetReverseRange() {
        // 下面是获取全部的，0 -1 表示获取全部的集合内容
        //Set<Object> results = redisHandler.zgetRange("fukun:subject", 0, -1);
        // 获取索引在0至2的科目，注意不是分数的区间
        Set<Object> results = redisHandler.zgetReverseRange("fukun:subject", 0, 2);
        if (!CollectionUtils.isEmpty(results)) {
            for(Object s : results) {
                System.out.println("成绩在70至79的科目，分数由高到低：" + s);
                System.out.println("=============================");
            }

        }
    }

    /**
     * 通过分数正序返回有序集合指定区间内的成员
     */
    @Test
    public void testZgetRangeByScore() {
        // 获取索引在70至79的科目
        Set<Object> results = redisHandler.zgetRangeByScore("fukun:subject", 70, 79);
        if (!CollectionUtils.isEmpty(results)) {
            for(Object s : results) {
                System.out.println("成绩在70至79的科目，分数由低到高：" + s);
                System.out.println("=============================");
            }

        }
    }

    /**
     * 通过分数倒序返回有序集合指定区间内的成员
     */
    @Test
    public void testZgetReverseRangeByScore() {
        // 获取索引在70至79的科目
        Set<Object> results = redisHandler.zgetReverseRangeByScore("fukun:subject", 70, 79);
        if (!CollectionUtils.isEmpty(results)) {
            for(Object s : results) {
                System.out.println("成绩在70至79的科目，分数由高到低：" + s);
                System.out.println("=============================");
            }

        }
    }
}
