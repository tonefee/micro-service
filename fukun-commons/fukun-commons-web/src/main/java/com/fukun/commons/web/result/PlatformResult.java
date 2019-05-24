package com.fukun.commons.web.result;

import com.fukun.commons.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 平台通用返回结果
 *
 * @author tangyifei
 * @since 2019-5-24 11:03:17
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlatformResult implements Result {

    private static final long serialVersionUID = 874200365941306385L;

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 成功返回信息-只包括状态码和状态信息
     *
     * @return 平台通用返回结果
     */
    public static PlatformResult success() {
        PlatformResult result = new PlatformResult();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    /**
     * 成功返回信息-包括状态码、状态信息、响应数据
     *
     * @param data 响应数据
     * @return 平台通用返回结果
     */
    public static PlatformResult success(Object data) {
        PlatformResult result = new PlatformResult();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    /**
     * 返回失败信息-只包括失败响应的状态码和状态信息
     *
     * @param resultCode 封装api统一返回状态码的对象
     * @return 平台通用返回结果
     */
    public static PlatformResult failure(ResultCode resultCode) {
        PlatformResult result = new PlatformResult();
        result.setResultCode(resultCode);
        return result;
    }

    /**
     * 返回失败信息-包括失败响应的状态码、状态信息、响应数据
     *
     * @param resultCode 封装api统一返回状态码的对象
     * @param data       响应数据
     * @return 平台通用返回结果
     */
    public static PlatformResult failure(ResultCode resultCode, Object data) {
        PlatformResult result = new PlatformResult();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    /**
     * 返回参数无效的失败信息
     *
     * @param message 参数无效的状态信息
     * @return 平台通用返回结果
     */
    public static PlatformResult failure(String message) {
        PlatformResult result = new PlatformResult();
        result.setCode(ResultCode.PARAM_IS_INVALID.code());
        result.setMsg(message);
        return result;
    }

    /**
     * 设置状态码与状态信息
     *
     * @param code 状态码
     */
    private void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }

}
