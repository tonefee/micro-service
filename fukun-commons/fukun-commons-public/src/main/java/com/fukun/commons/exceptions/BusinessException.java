package com.fukun.commons.exceptions;

import com.fukun.commons.enums.BusinessExceptionEnum;
import com.fukun.commons.enums.ResultCode;
import com.fukun.commons.util.StringUtil;
import lombok.Data;

/**
 * 自定义业务异常类
 *
 * @author tangyifei
 * @since 2019-5-23 10:16:03 PM
 */
@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 194906846739586856L;

    /**
     * 业务异常状态码
     */
    protected String code;

    /**
     * 业务异常信息
     */
    protected String message;

    /**
     * api统一结果状态码对象
     */
    protected ResultCode resultCode;

    /**
     * 异常相关的数据信息
     */
    protected Object data;

    /**
     * 构造业务异常对象
     */
    public BusinessException() {
        // 根据异常类返回该异常类对应的http状态码、resultCode中api统一返回信息
        BusinessExceptionEnum exceptionEnum = BusinessExceptionEnum.getByEClass(this.getClass());
        if (exceptionEnum != null) {
            // resultCode中自定义的api统一返回信息对象
            resultCode = exceptionEnum.getResultCode();
            // resultCode中api统一返回信息对象中的自定义返回码
            code = exceptionEnum.getResultCode().code().toString();
            // resultCode中api统一返回信息对象中的自定义返回信息
            message = exceptionEnum.getResultCode().message();
        }

    }

    /**
     * 构造业务异常对象
     *
     * @param message 自定义消息
     */
    public BusinessException(String message) {
        this();
        this.message = message;
    }

    /**
     * 格式化消息
     *
     * @param format  格式
     * @param objects 可变参数
     */
    public BusinessException(String format, Object... objects) {
        this();
        this.message = StringUtil.formatIfArgs(format, "{}", objects);
    }

    public BusinessException(ResultCode resultCode, Object data) {
        this(resultCode);
        this.data = data;
    }

    public BusinessException(ResultCode resultCode) {
        this.resultCode = resultCode;
        this.code = resultCode.code().toString();
        this.message = resultCode.message();
    }

}
