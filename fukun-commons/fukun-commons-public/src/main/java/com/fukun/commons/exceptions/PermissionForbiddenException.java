package com.fukun.commons.exceptions;


import com.fukun.commons.enums.ResultCode;

/**
 * 权限不足异常
 *
 * @author tangyifei
 * @since 2019-5-23 10:48:25 PM
 */
public class PermissionForbiddenException extends BusinessException {

    private static final long serialVersionUID = 3721036867889297081L;

    public PermissionForbiddenException() {
        super();
    }

    public PermissionForbiddenException(Object data) {
        super.data = data;
    }

    public PermissionForbiddenException(ResultCode resultCode) {
        super(resultCode);
    }

    public PermissionForbiddenException(ResultCode resultCode, Object data) {
        super(resultCode, data);
    }

    public PermissionForbiddenException(String msg) {
        super(msg);
    }

    public PermissionForbiddenException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

}
