package com.fukun.commons.enums;

/**
 * 调用来源枚举类
 *
 * @author tangyifei
 * @since 2019-5-23 10:01:52 PM
 */
public enum CallSourceEnum {

    /**
     * WEB网站
     */
    WEB,

    /**
     * PC客户端
     */
    PC,

    /**
     * 微信公众号
     */
    WECHAT,

    /**
     * IOS平台
     **/
    IOS,

    /**
     * 安卓平台
     */
    ANDROID;

    /**
     * 校验枚举的有效性
     *
     * @param name 待校验的枚举值
     * @return 校验是否成功
     */
    public static boolean isValid(String name) {
        for(CallSourceEnum callSource : CallSourceEnum.values()) {
            if (callSource.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
