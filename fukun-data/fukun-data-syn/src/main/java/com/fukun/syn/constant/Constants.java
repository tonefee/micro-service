package com.fukun.syn.constant;

/**
 * 常量类
 *
 * @author tangyifei
 * @date 2019年7月5日16:21:48
 */
public final class Constants {

    /**
     * 最大重发次数
     */
    public final static int MAX_TRY_COUNT = 3;

    /**
     * 最大重发次数相关的key
     */
    public final static String MAX_TRY_COUNT_PREFIX_KEY = "retry:";

    /**
     * 广播交换机的名称
     */
    public final static String FANOUT_EXCHANGE_NAME = "data_syn_fanout_exchange";

    /**
     * 队列的名称
     */
    public final static String FANOUT_QUEUE_NAME = "data_syn_queue";

}
