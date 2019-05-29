package com.fukun.commons.enums;

import com.fukun.commons.util.StringUtil;

/**
 * 统一定义缓存KEY
 * 备注：
 * ① 枚举name应遵守以VALUE、LIST、SET、ZSET、HASH等开头
 * ② 枚举code应尽量简写形式，以工程主名字为开头，例如：fukun、fukun_push
 * ③ `:` 冒号分割的前后应该是一个名词词性，冒号前后是有上下级关系的，多个单词解释一个名词时，约定使用 `_`下划线分割
 * <p>
 * 举例：
 * ① FUKUN的用户缓存 fukun:user:{userId}
 * ② FUKUN的某用户地址信息缓存 fukun:user_address
 * <p>
 *
 * @author tangyifei
 * @since 2019-5-23 09:50:21 AM
 */
public enum CacheKeyEnum {

    /* ---------------用户相关缓存------------------ */
    /**
     * 登录TOKEN缓存key，生存时长为一周
     */
    VALUE_LOGIN_TOKENS("fukun:login_tokens:", TimeEnum.ONE_WEEK.sec()),

    /**
     * 用户缓存，生存时长为一周
     */
    VALUE_USERS("fukun:user:profile:%s", TimeEnum.ONE_WEEK.sec());

    /**
     * 缓存key
     */
    private String code;

    /**
     * 过期时间（单位：秒）
     */
    private Integer sec;

    CacheKeyEnum(String code, Integer sec) {
        this.code = code;
        this.sec = sec;
    }

    public String code() {
        return this.code;
    }

    public Integer sec() {
        return this.sec;
    }

    @Override
    public String toString() {
        return this.name();
    }

    /**
     * 格式化相关的key
     *
     * @param args 可变参数
     * @return 格式化后的key
     */
    public String formatKey(Object... args) {
        // 判断%s在this.code（缓存key）中的出现的次数
        int requiredNum = StringUtil.getSubStrCount(this.code, "%s");
        boolean isCorrectArgsNum = requiredNum != 0 && (args == null || args.length != requiredNum);
        if (isCorrectArgsNum) {
            throw new IllegalArgumentException("The number of parameters is not equal to the required number.");
        }
        return String.format(this.code, args);
    }
}
