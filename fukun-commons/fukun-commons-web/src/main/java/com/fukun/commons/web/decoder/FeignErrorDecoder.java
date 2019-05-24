package com.fukun.commons.web.decoder;

import com.alibaba.fastjson.JSON;
import com.fukun.commons.exceptions.BusinessException;
import com.fukun.commons.util.JsonUtil;
import com.fukun.commons.web.result.DefaultErrorResult;
import feign.Response;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Feign错误解码器类
 *
 * @author tangyifei
 * @since 2019-5-23 17:11:00 PM
 */
@Slf4j
public class FeignErrorDecoder extends feign.codec.ErrorDecoder.Default {

    @Override
    public Exception decode(String methodKey, Response response) {

        // 解析调用远端api出现的异常
        Exception defaultResultE = super.decode(methodKey, response);

        // 是否出现重试相关的异常，如果抛出的异常类型是重试异常直接返回
        if (RetryableException.class.equals(defaultResultE.getClass())) {
            return defaultResultE;
        }

        // 异常信息，FeignClient默认截获的异常如下：
        // {
        //	"timestamp": "2019-02-24 17:15:19",
        //	"status": 500,
        //	"error": "Internal Server Error",
        //	"message": "status 400 reading PaymentInterface#methodName(ParamType,ParamType)
        //  "content": {"type":"http://httpstatus.es/404","title":"未找到资源","status":400,"detail":"这里是详细的异常信息"} ",
        //	"path": "/oauth/token"
        //}
        String message = defaultResultE.getMessage();
        String separator = "content:";
        // substringAfterLast方法取message内分隔符content:后的字符串。
        // 就是取如上的{"type":"http://httpstatus.es/404","title":"未找到资源","status":400,"detail":"这里是详细的异常信息"}
        String content = StringUtils.substringAfterLast(message, separator);
        DefaultErrorResult defaultResult = JSON.parseObject(content, DefaultErrorResult.class);
        if (defaultResult == null) {
            log.error("FeignErrorDecoder occurs error, defaultResult is null, response:{}", JsonUtil.object2Json(response));
            return defaultResultE;
        }

        if (defaultResult.getStatus() != null && defaultResult.getCode() != null) {
            try {
                BusinessException businessException = (BusinessException) Class.forName(defaultResult.getException()).newInstance();
                businessException.setCode(defaultResult.getCode().toString());
                businessException.setMessage(defaultResult.getMessage());
                businessException.setData(defaultResult.getErrors());
                return businessException;
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                log.error("business exception cast error, caused by: ", e);
            }
        }
        return defaultResultE;
    }

}
