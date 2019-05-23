package com.fukun.commons.exceptions;


import com.fukun.commons.enums.ResultCode;

/**
 * 数据已经存在异常
 *
 * @author tangyifei
 * @since 2019-5-23 10:37:11 AM
 */
public class DataConflictException extends BusinessException {

    private static final long serialVersionUID = 3721036867889297081L;

    public DataConflictException() {
        super();
    }

    public DataConflictException(Object data) {
        super.data = data;
    }

    public DataConflictException(ResultCode resultCode) {
        super(resultCode);
    }

    public DataConflictException(ResultCode resultCode, Object data) {
        super(resultCode, data);
    }

    public DataConflictException(String msg) {
        super(msg);
    }

    public DataConflictException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }


}
