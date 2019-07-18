package com.fukun.syn.constant;

/**
 * 消息中间件类型枚举
 *
 * @author tangyifei
 * @date 2019年7月18日12:28:32
 */
public enum MessageComponentTypeEnums {

    RABBITMQ(1),

    REDIS(2),

    KAFKA(3),

    ROCKET(4);

    private final int type;

    MessageComponentTypeEnums(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
