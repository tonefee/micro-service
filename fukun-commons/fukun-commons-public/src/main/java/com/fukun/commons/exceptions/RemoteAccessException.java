package com.fukun.commons.exceptions;


import com.fukun.commons.enums.ResultCode;

/**
 * 远程访问异常
 *
 * @author tangyifei
 * @since 2019-5-23 10:49:09 PM
 */
public class RemoteAccessException extends BusinessException {

    private static final long serialVersionUID = -832464574076215195L;

    public RemoteAccessException() {
        super();
    }

    public RemoteAccessException(Object data) {
        super.data = data;
    }

    public RemoteAccessException(ResultCode resultCode) {
        super(resultCode);
    }

    public RemoteAccessException(ResultCode resultCode, Object data) {
        super(resultCode, data);
    }

    public RemoteAccessException(String msg) {
        super(msg);
    }

    public RemoteAccessException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

}
