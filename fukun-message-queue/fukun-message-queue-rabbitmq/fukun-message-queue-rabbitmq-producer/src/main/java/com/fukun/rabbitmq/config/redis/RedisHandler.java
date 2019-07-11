package com.fukun.rabbitmq.config.redis;

import com.alibaba.fastjson.JSON;
import com.fukun.commons.util.CollectionUtil;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis基本操作类
 *
 * @author tangyifei
 * @date 2019年7月6日11:43:28
 */
public class RedisHandler {

    /**
     * 不设置过期时长
     */
    private final static long NOT_EXPIRE = -1;

    /**
     * 默认过期时长，单位：秒
     */
    private final static long DEFAULT_EXPIRE = 60 * 60 * 24;

    /**
     * redis模板引擎
     */
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 实例化redis模板引擎
     *
     * @param redisTemplate redis模板引擎
     */
    public RedisHandler(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //=============================共享的操作开始============================

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 查找所有符合给定模式 pattern 的 key
     * 如下面几个例子 KEYS * 匹配数据库中所有 key 。 KEYS h?llo 匹配 hello ， hallo 和
     * hxllo 等。 KEYS h*llo 匹配 hllo 和 heeeeello 等。 KEYS h[ae]llo 匹配
     * hello 和 hallo ，但不匹配 hillo 。
     *
     * @param pattern 模式
     * @return 符合给定模式的 key 列表。
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 删除所有符合给定模式 pattern 的 key
     *
     * @param pattern 模式
     */
    public void deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (CollectionUtil.isNotEmpty(keys)) {
            for(String key : keys) {
                redisTemplate.delete(key);
            }
        }
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    private void expire(String key, long time) {
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    //=============================共享的操作结束============================

    //============================针对字符串的操作开始(针对字符串的操作，一个键最大能存储512MB, 在redis中，存储的字符串都是以二级制的形式存在的)=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 相关键对应的值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取key的值并且转化为指定的对象
     *
     * @param key   键
     * @param clazz 指定的类
     * @param <T>   指定的对象类型
     * @return 指定的对象
     */
    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    /**
     * 获取key对应的某一个范围的字符串值
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 指定的值
     */
    public String getRange(String key, long start, long end) {
        return redisTemplate.opsForValue().get(key, start, end);
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     *
     * @param key   键
     * @param value key 对应的新值
     * @return key 对应的旧值
     */
    public Object getSet(String key, Object value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)。
     *
     * @param key    键
     * @param offset 偏移量
     * @return 获取bit位成功与否
     */
    public Boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 获取所有(一个或多个)给定 key 的值。
     *
     * @param keys 键的集合
     * @return 值的列表
     */
    @SuppressWarnings("unchecked")
    public List<?> multiGet(String... keys) {
        return redisTemplate.opsForValue().multiGet(CollectionUtils.arrayToList(keys));
    }

    /**
     * 返回 key 所储存的字符串值的长度
     *
     * @param key 键
     * @return 字符串值的长度
     */
    public Long getStrLen(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    /**
     * 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。
     *
     * @param key    键
     * @param offset 偏移量
     * @param value  值
     * @return 设置bit成功与否
     */
    public Boolean setBit(String key, long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public void set(String key, Object value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, toJson(value), time, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
    }

    /**
     * 只有在 key 不存在时，普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public Boolean setIfAbsent(String key, Object value, long time) {
        if (time > 0) {
            return redisTemplate.opsForValue().setIfAbsent(key, toJson(value), time, TimeUnit.SECONDS);
        } else {
            return setIfAbsent(key, value);
        }
    }

    /**
     * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始。
     *
     * @param key    键
     * @param value  值
     * @param offset 偏移量
     */
    public void setOffset(String key, Object value, long offset) {
        redisTemplate.opsForValue().set(key, value, offset);
    }

    /**
     * 同时设置一个或多个 key-value 对
     *
     * @param map 多个键值对
     */
    public void multiSet(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
     *
     * @param map 多个键值对
     * @return 设置成功与否
     */
    public Boolean multiSetIfAbsent(Map<String, Object> map) {
        return redisTemplate.opsForValue().multiSetIfAbsent(map);
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     */
    public void incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 将 key 所储存的值加上给定的浮点增量值（increment）
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     */
    public Double incr(String key, double delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递增1
     *
     * @param key 键
     */
    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 递减1
     *
     * @param key 键
     */
    public Long decr(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾
     *
     * @param key   键
     * @param value 值
     */
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    /**
     * 根据指定过期时长的key来获取指定的值并且转化为指定的对象
     *
     * @param key    键
     * @param clazz  指定的类
     * @param expire 过期时间（秒）
     * @param <T>    指定的对象类型
     * @return 指定的对象
     */
    @SuppressWarnings("unchecked")
    private <T> T get(String key, Class<T> clazz, long expire) {
        String value = toJson(redisTemplate.opsForValue().get(key));
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    /**
     * 只有在 key 不存在时设置 key 的值
     *
     * @param key   键
     * @param value 值
     * @return 设置成功与否
     */
    private Boolean setIfAbsent(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    //============================针对字符串的操作结束=============================

    //================================针对Map的操作开始(Redis hash是一个string类型的field和value的映射表，hash特别适合用于存储对象。每个 hash 可以存储 2^32 - 1 键值对（40多亿个键值对）。)=================================

    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key   键 不能为null
     * @param field 域 不能为null
     * @return 值
     */
    public Object hget(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key    键 不能为null
     * @param fields 域  不能为null
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public List<?> hmget(String key, String... fields) {
        return redisTemplate.opsForHash().multiGet(key, CollectionUtils.arrayToList(fields));
    }

    /**
     * 获取在哈希表中指定 key 的所有字段和值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取所有哈希表中的字段
     *
     * @param key 键
     * @return key对应的所有字段
     */
    public Set<Object> hKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取哈希表中字段的数量
     *
     * @param key 键
     * @return key对应的字段数量
     */
    public Long hLen(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * HashSet同时将多个 field-value (域-值)对设置到哈希表 key 中
     *
     * @param key 键
     * @param map 对应多个键值
     */
    public void hmset(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * HashSet同时将多个 field-value (域-值)对设置到哈希表 key 中, 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     */
    public void hmset(String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(key, map);
        expire(key, time);
    }

    /**
     * 将哈希表 key 中的字段 field 的值设为 value
     *
     * @param key   键
     * @param field 域
     * @param value 值
     */
    public void hset(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 只有在字段 field 不存在时，设置哈希表字段的值。
     *
     * @param key   键
     * @param field 域
     * @param value 值
     */
    public Boolean hsetIfAbsent(String key, String field, Object value) {
        return redisTemplate.opsForHash().putIfAbsent(key, field, value);
    }

    /**
     * 将哈希表 key 中的字段 field 的值设为 value，并设置超时时间
     *
     * @param key   键
     * @param field 域
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     */
    public void hset(String key, String field, Object value, long time) {
        redisTemplate.opsForHash().put(key, field, value);
        expire(key, time);
    }

    /**
     * 只有在字段 field 不存在时，设置哈希表字段的值，并设置超时时间
     *
     * @param key   键
     * @param field 域
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     */
    public void hsetIfAbsent(String key, String field, Object value, long time) {
        redisTemplate.opsForHash().putIfAbsent(key, field, value);
        expire(key, time);
    }

    /**
     * 删除hash表中的值，删除一个或多个哈希表字段
     *
     * @param key    键 不能为null
     * @param fields 域 可以是多个项，但是不能为null（如果存储对象，表示的是对象的多个属性）
     */
    public Long hdel(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 判断hash表中是否有该项的值，查看哈希表 key 中，指定的字段是否存在
     * 如果存储的是对象，就是说查看某个对象的某个属性是否存在
     *
     * @param key   键 不能为null
     * @param field 域 不能为null
     * @return true 存在 false不存在
     */
    public Boolean hHasKey(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * 为哈希表 key 中的指定字段的浮点数值加上增量 increment
     *
     * @param key   键
     * @param field 域
     * @param by    要增加几(大于0)
     * @return 结果
     */
    public Double hincr(String key, String field, double by) {
        return redisTemplate.opsForHash().increment(key, field, by);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key   键
     * @param field 域
     * @param by    要增加几(大于0)
     * @return 结果
     */
    public Long hincr(String key, String field, long by) {
        return redisTemplate.opsForHash().increment(key, field, by);
    }

    /**
     * hash递减，针对浮点型
     *
     * @param key   键
     * @param field 域
     * @param by    要减少记(小于0)
     * @return 递减结果
     */
    public double hdecr(String key, String field, double by) {
        return redisTemplate.opsForHash().increment(key, field, -by);
    }

    /**
     * hash递减
     *
     * @param key   键
     * @param field 域
     * @param by    要减少记(小于0)
     * @return 递减结果
     */
    public Long hdecr(String key, String field, long by) {
        return redisTemplate.opsForHash().increment(key, field, -by);
    }

    /**
     * 获取哈希表中所有值
     *
     * @param key 键
     * @return key 对应的值集合
     */
    public List<?> hvalues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 迭代哈希表中的键值对
     *
     * @param key 键
     * @param var 扫描项
     * @return 符合条件的键值对游标对象
     */
    public Cursor<Map.Entry<Object, Object>> hscan(String key, ScanOptions var) {
        return redisTemplate.opsForHash().scan(key, var);
    }

    //================================针对Map的操作结束=================================

    //============================针对set的操作开始（Redis的Set是string类型的无序集合，集合成员是唯一的，这就意味着集合中不能出现重复的数据，redis中的集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是O(1)，集合中最大的成员数为 2^32 - 1 (4294967295, 每个集合可存储40多亿个成员)。）=============================

    /**
     * 根据key获取Set中的所有值，即返回集合中的所有成员
     *
     * @param key 键
     * @return key对应set集合中的多个值
     */
    public Set<Object> sGet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 根据value从一个set中查询,是否存在，即判断 member 元素是否是集合 key 的成员
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Boolean sHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 向集合中添加一个或多个成员
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 插入的个数
     */
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 向集合中添加一个或多个成员并设置超时时间
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     */
    public void sAdd(String key, long time, Object... values) {
        redisTemplate.opsForSet().add(key, values);
        expire(key, time);
    }

    /**
     * 返回给定所有集合的差集（比较多个key中的不同的值）
     *
     * @param key      键
     * @param otherKey 其他键
     * @return 差集
     */
    public Set<Object> sDiff(String key, String otherKey) {
        return redisTemplate.opsForSet().difference(key, otherKey);
    }

    /**
     * 返回给定所有集合的差集，对多个key进行比较（比较多个key中的不同的值）
     *
     * @param key       键
     * @param otherKeys 多个键
     * @return 差集
     */
    public Set<Object> sDiff(String key, List<String> otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * 返回给定所有集合的差集并存储在 destination 中（比较多个key中的不同的值）
     *
     * @param key      键
     * @param otherKey 其他键
     * @param destKey  目标key
     * @return 插入到key3中的差集个数
     */
    public Long sDiffStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
    }

    /**
     * 返回给定所有集合的差集并存储在 destination 中，对多个key进行比较（比较多个key中的不同的值）
     *
     * @param key       键
     * @param otherKeys 其他键
     * @param destKey   目标key
     * @return 插入到key3中的差集个数
     */
    public Long sDiffStore(String key, List<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys, destKey);
    }

    /**
     * 返回给定所有集合的交集（比较多个key中的相同的值）
     *
     * @param key      键
     * @param otherKey 其他键
     * @return 交集
     */
    public Set<Object> sInter(String key, String otherKey) {
        return redisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * 返回给定所有集合的交集，对多个key进行比较（比较多个key中的相同的值）
     *
     * @param key       键
     * @param otherKeys 其他多个键
     * @return 交集
     */
    public Set<Object> sInter(String key, List<String> otherKeys) {
        return redisTemplate.opsForSet().intersect(key, otherKeys);
    }

    /**
     * 返回给定所有集合的交集并存储在 destination 中（比较多个key中的相同的值）
     *
     * @param key      键
     * @param otherKey 其他键
     * @param destKey  目标key
     * @return 交集
     */
    public Long sInterStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * 返回给定所有集合的交集并存储在 destination 中，对多个key进行比较（比较多个key中的相同的值）
     *
     * @param key       键
     * @param otherKeys 其他键
     * @param destKey   目标key
     * @return 交集
     */
    public Long sInterStore(String key, List<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * 返回所有给定集合的并集（合并多个key中值）
     *
     * @param key      键
     * @param otherKey 其他键
     * @return 并集
     */
    public Set<Object> sUnion(String key, String otherKey) {
        return redisTemplate.opsForSet().union(key, otherKey);
    }

    /**
     * 返回所有给定集合的并集（合并多个key中值）
     *
     * @param key       键
     * @param otherKeys 其他键
     * @return 并集
     */
    public Set<Object> sUnion(String key, List<String> otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * 所有给定集合的并集存储在 destination 集合中（合并多个key中值）
     *
     * @param key      键
     * @param otherKey 其他键
     * @param destKey  目标key
     * @return 并集
     */
    public Long sUnionStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * 所有给定集合的并集存储在 destination 集合中（合并多个key中值）
     *
     * @param key       键
     * @param otherKeys 其他键
     * @param destKey   目标key
     * @return 并集
     */
    public Long sUnionStore(String key, List<String> otherKeys, String destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 将 value 元素从 source 集合移动到 destination 集合
     *
     * @param key     原key
     * @param value   值
     * @param destKey 目标键
     * @return true 移动成功  false 移动失败
     */
    public Boolean sMove(String key, Object value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 移除并返回集合中的一个随机元素
     *
     * @param key 键
     * @return 随机元素值
     */
    public Object sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 移除并返回集合中的多个随机元素
     *
     * @param key   键
     * @param count 数量
     * @return 随机元素值列表
     */
    public List<Object> sPop(String key, long count) {
        return redisTemplate.opsForSet().pop(key, count);
    }

    /**
     * 返回集合中一个随机数但是不移除
     *
     * @param key 键
     * @return 随机元素值
     */
    public Object sRandomMembers(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 返回集合中多个随机数但是不移除
     *
     * @param key   键
     * @param count 数量
     * @return 随机元素值列表
     */
    public List<Object> sRandomMembers(String key, long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 返回集合中多个随机数但是不移除并且不重复获取
     *
     * @param key   键
     * @param count 数量
     * @return 随机元素值列表
     */
    public Set<Object> sDistinctRandomMembers(String key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * 获取set集合的大小
     *
     * @param key 键
     * @return 集合的大小
     */
    public Long sGetSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 移除集合中的元素
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    //============================针对set的操作结束=============================

    // ============================针对zset的操作开始（Redis zset 和 set 一样也是string类型元素的集合,且不允许重复的成员。不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。zset的成员是唯一的,但分数(score)却可以重复。）=============================

    /**
     * 添加一个元素, zset与set最大的区别就是每个元素都有一个score，因此有个排序的辅助功能;  zadd
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return 添加成功与否，true 添加成功 false 添加失败
     */
    public Boolean zsetAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 获取有序集合的成员数
     *
     * @param key 键
     * @return 有序集合的成员数
     */
    public Long zsetSize(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 计算在有序集合中指定区间分数的成员数
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 指定区间分数的成员数
     */
    public Long zsetCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * score的增加or减少 zincrby
     * zset中的元素塞入之后，可以修改其score的值，通过 zincrby 来对score进行加/减；当元素不存在时，则会新插入一个
     * zincrby 与 zadd 最大的区别是前者是增量修改；后者是覆盖score方式
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     * @return 增加或者减少后的分数
     */
    public Double incrScore(String key, String value, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中
     *
     * @param key      键
     * @param otherKey 其他键
     * @param destKey  目标键
     * @return 插入到目标key对应的集合中的交集个数
     */
    public Long zsetInterStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中
     *
     * @param key       键
     * @param otherKeys 其他键
     * @param destKey   目标键
     * @return 插入到目标key对应的集合中的交集个数
     */
    public Long zsetInterStore(String key, List<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * 计算给定的一个或多个有序集的并集并将结果集存储在新的有序集合 key 中
     *
     * @param key      键
     * @param otherKey 其他键
     * @param destKey  目标键
     * @return 插入到目标key对应的集合中的并集个数
     */
    public Long zsetUnionStore(String key, String otherKey, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * 计算给定的一个或多个有序集的并集并将结果集存储在新的有序集合 key 中
     *
     * @param key       键
     * @param otherKeys 其他键
     * @param destKey   目标键
     * @return 插入到目标key对应的集合中的并集个数
     */
    public Long zsetUnionStore(String key, List<String> otherKeys, String destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 通过索引区间返回有序集合成指定区间内的成员
     * 查询集合中指定顺序的值， 0 -1 表示获取全部的集合内容  zrange
     * 返回有序的集合，score小的在前面
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 有序集合
     */
    public Set<Object> zsetRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 返回有序集中指定区间内的成员，通过索引，分数从高到底
     * 查询集合中指定顺序的值， 0 -1 表示获取全部的集合内容  zrange
     * 返回有序的集合，score小的在前面
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 有序集合
     */
    public Set<Object> zsetReverseRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 通过分数返回有序集合指定区间内的成员
     * 返回有序的集合，score小的在前面
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 有序集合
     */
    public Set<Object> zsetRangeByScore(String key, double start, double end) {
        return redisTemplate.opsForZSet().rangeByScore(key, start, end);
    }

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     * 返回有序的集合，score小的在前面
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 有序集合
     */
    public Set<Object> zsetReverseRangeByScore(String key, double start, double end) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, start, end);
    }

    /**
     * 返回有序集合中指定成员的索引
     * 获取排名；这里score越小排名越高;
     * 用zset来做排行榜可以很简单的获取某个用户在所有人中的排名与积分
     *
     * @param key   键
     * @param value 值
     * @return 排名
     */
    public Long zsetRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序
     *
     * @param key   键
     * @param value 值
     * @return 排名
     */
    public Long zsetReverseRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 移除有序集合中的一个或多个成员
     *
     * @param key    键
     * @param values 多个值
     * @return 删除的元素数目
     */
    public Long zsetRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 移除有序集合中给定的分数区间的所有成员
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 删除的元素数目
     */
    public Long zsetRemoveRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 移除有序集合中给定的索引区间的所有成员
     *
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 删除的元素数目
     */
    public Long zsetRemoveRange(String key, long min, long max) {
        return redisTemplate.opsForZSet().removeRange(key, min, max);
    }

    /**
     * 查询value对应的score, zscore
     * 当value在集合中时，返回其score；如果不在，则返回null
     *
     * @param key   键
     * @param value 值
     * @return 分数
     */
    public Double zsetScore(String key, String value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 查询集合中指定顺序的值和score，0, -1 表示获取全部的集合内容
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 有序集合
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScore(String key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    //============================针对zset的操作结束=============================

    //===============================针对list的操作开始（列表最多可存储 2^32 - 1 元素 (4294967295, 每个列表可存储40多亿)。Redis列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边））=================================

    /**
     * 从列表的左边移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     键
     * @param timeout 超时时间（秒）
     * @return 键对应的值
     */
    public Object lpop(String key, long timeout) {
        return redisTemplate.opsForList().leftPop(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 从列表的左边移出并获取列表的第一个元素
     *
     * @param key 键
     * @return 键对应的值
     */
    public Object lpop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从列表的右边移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     键
     * @param timeout 超时时间（秒）
     * @return 键对应的值
     */
    public Object rpop(String key, long timeout) {
        return redisTemplate.opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 从列表的右边移出并获取列表的第一个元素
     *
     * @param key 键
     * @return 键对应的值
     */
    public Object rpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key1    键1
     * @param key2    键2
     * @param timeout 超时时间（秒）
     * @return 键对应的值
     */
    public Object rpopLpush(String key1, String key2, long timeout) {
        return redisTemplate.opsForList().rightPopAndLeftPush(key1, key2, timeout, TimeUnit.SECONDS);
    }

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它
     *
     * @param key1 键1
     * @param key2 键2
     * @return 键对应的值
     */
    public Object rpopLpush(String key1, String key2) {
        return redisTemplate.opsForList().rightPopAndLeftPush(key1, key2);
    }

    /**
     * 通过索引获取列表中的元素
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 索引位置对应的元素
     */
    public Object lGetIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取列表指定范围内的元素
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return 值的范围
     */
    public List<Object> lGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return 列表的长度
     */
    public Long lGetListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 从列表的右边插入元素
     *
     * @param key   键
     * @param value 值
     * @return 插入的个数
     */
    public Long lrPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 从列表的右边插入元素，并设置超时时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     */
    public void lrPush(String key, Object value, long time) {
        redisTemplate.opsForList().rightPush(key, value);
        expire(key, time);
    }

    /**
     * 从列表的右边插入多个元素
     *
     * @param key    键
     * @param values 值的列表
     * @return 插入的个数
     */
    public Long lrPushAll(String key, List<Object> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 从列表的右边插入元素，如果值不存在
     *
     * @param key   键
     * @param value 值
     * @return 插入的个数
     */
    public Long lrPushIfPresent(String key, Object value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 从列表的左边插入元素，如果值不存在
     *
     * @param key   键
     * @param value 值
     * @return 插入的个数
     */
    public Long llPushIfPresent(String key, Object value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    /**
     * 从列表的左边插入元素，插入列表的头部
     *
     * @param key   键
     * @param value 值
     * @return 插入的个数
     */
    public Long llPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 从列表的左边插入元素，并设置超时时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 超时时间（秒）
     */
    public void llPush(String key, Object value, long timeout) {
        redisTemplate.opsForList().leftPush(key, value);
        expire(key, timeout);
    }

    /**
     * 从列表的左边插入多个元素
     *
     * @param key    键
     * @param values 值的列表
     * @return 插入的个数
     */
    public Long llPushAll(String key, List<Object> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public void lUpdateIndex(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束为止
     */
    public void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }
    //===============================针对list的操作结束=================================

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }
}
