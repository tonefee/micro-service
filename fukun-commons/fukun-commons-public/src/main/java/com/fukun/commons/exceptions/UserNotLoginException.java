package com.fukun.commons.exceptions;

/**
 * 用户未登录异常
 *
 * @author tangyifei
 * @since 2019-5-23 10:49:44 PM
 */
public class UserNotLoginException extends BusinessException {

    private static final long serialVersionUID = -1879503946782379204L;

    public UserNotLoginException() {
        super();
    }

    public UserNotLoginException(String msg) {
        super(msg);
    }

    public UserNotLoginException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

}
