package com.fukun.rabbitmq.constant;

/**
 * 常量类
 *
 * @author tangyifei
 * @date 2019年7月5日16:21:48
 */
public final class Constants {

    /**
     * 发送中
     */
    public static final String ORDER_SENDING = "0";

    /**
     * 发送成功
     */
    public static final String ORDER_SEND_SUCCESS = "1";

    /**
     * 发送失败
     */
    public static final String ORDER_SEND_FAILURE = "2";

    /**
     * 分钟超时单位：min
     */
    public static final int ORDER_TIMEOUT = 1;

    /**
     * 最大重发次数
     */
    public final static int MAX_TRY_COUNT = 3;

    /**
     * 最大重发次数相关的key
     */
    public final static String MAX_TRY_COUNT_PREFIX_KEY = "retry:";

}
