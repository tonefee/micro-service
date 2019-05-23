package com.fukun.commons.enums;

/**
 * 接口返回值风格样式枚举类
 *
 * @author tangyifei
 * @since 2019-5-23 09:30:17 PM
 */
public enum ApiStyleEnum {

    /**
     * 接口返回值无相关的风格样式
     */
    NONE;

    /**
     * 验证枚举值的有效性
     *
     * @param name 枚举名称
     * @return 验证结果
     */
    public static boolean isValid(String name) {
        for(ApiStyleEnum callSource : ApiStyleEnum.values()) {
            if (callSource.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
