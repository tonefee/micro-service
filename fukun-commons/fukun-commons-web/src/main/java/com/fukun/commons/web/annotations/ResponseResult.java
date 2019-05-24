package com.fukun.commons.web.annotations;

import com.fukun.commons.web.result.PlatformResult;
import com.fukun.commons.web.result.Result;

import java.lang.annotation.*;

/**
 * 接口返回结果增强  会通过拦截器拦截后放入标记，在WebResponseBodyHandler进行结果处理
 *
 * @author tangyifei
 * @since 2019-5-23 17:02:16 PM
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseResult {

    Class<? extends Result> value() default PlatformResult.class;

}
