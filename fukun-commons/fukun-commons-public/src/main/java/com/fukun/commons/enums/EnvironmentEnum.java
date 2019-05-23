package com.fukun.commons.enums;

import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

/**
 * 环境常量枚举
 *
 * @author tangyifei
 * @since 2019-5-23 10:03:43 PM
 */
public enum EnvironmentEnum {

    /**
     * 线上，生产环境
     */
    PROD,

    /**
     * 联调
     */
    FE,

    /**
     * 测试
     */
    QA;

    public static boolean isProdEnv(Environment env) {
        Assert.notNull(env, "env parameter not null.");

        // 验证是否为生产环境
        return EnvironmentEnum.PROD.name().equalsIgnoreCase(env.getProperty("spring.profiles.active"));
    }

    @Override
    public String toString() {
        return this.name();
    }

}
