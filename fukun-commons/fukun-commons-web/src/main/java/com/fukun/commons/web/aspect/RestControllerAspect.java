package com.fukun.commons.web.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fukun.commons.util.IpUtil;
import com.fukun.commons.web.constants.HeaderConstants;
import com.fukun.commons.web.handler.GlobalExceptionHandler;
import com.fukun.user.model.bo.LoginUser;
import com.fukun.user.token.helper.LoginTokenHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 请求参数、响应体统一日志打印
 *
 * @author tangyifei
 * @since 2019-5-23 17:03:31 PM
 */
@Slf4j
@Aspect
public class RestControllerAspect {

    /**
     * 环绕通知
     *
     * @param joinPoint 连接点
     * @return 切入点返回值
     * @throws Throwable 异常信息
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController) || @annotation(org.springframework.web.bind.annotation.RestController)")
    public Object apiLog(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 为true需要记录日志，为false就是不记录日志
        boolean logFlag = this.needToLog(method);
        // 当不记录日志的情况下，执行目标方法
        if (!logFlag) {
            return joinPoint.proceed();
        }

        // 通过线程安全模式获取上下文请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 从请求中获取token中的登录用户信息
        LoginUser loginUser = LoginTokenHelper.getLoginUserFromRequest();

        // 获取请求中的ip地址
        String ip = IpUtil.getRealIp(request);
        // 获取目标方法名称
        String methodName = this.getMethodName(joinPoint);
        // 获取目标方法参数信息并且移除敏感内容
        String params = this.getParamsJson(joinPoint);
        // 获取登录用户的id
        String requester = loginUser == null ? "unknown" : String.valueOf(loginUser.getId());
        // 获取调用来源，比如web网站、微信、ios、安卓
        String callSource = request.getHeader(HeaderConstants.CALL_SOURCE);
        // 获取app的版本号
        String appVersion = request.getHeader(HeaderConstants.APP_VERSION);
        // 获取api的版本号
        String apiVersion = request.getHeader(HeaderConstants.API_VERSION);
        // 获取用户的引擎
        String userAgent = request.getHeader("user-agent");

        log.info("Started request requester [{}] method [{}] params [{}] IP [{}] callSource [{}] appVersion [{}] apiVersion [{}] userAgent [{}]", requester, methodName, params, ip, callSource, appVersion, apiVersion, userAgent);
        long start = System.currentTimeMillis();
        // 执行目标方法
        Object result = joinPoint.proceed();
        log.info("Ended request requester [{}] method [{}] params[{}] response is [{}] cost [{}] millis ",
                requester, methodName, params, this.deleteSensitiveContent(result), System.currentTimeMillis() - start);
        return result;
    }

    /**
     * 获取目标方法名称
     *
     * @param joinPoint 连接点
     * @return
     */
    private String getMethodName(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        String shortMethodNameSuffix = "(..)";
        if (methodName.endsWith(shortMethodNameSuffix)) {
            methodName = methodName.substring(0, methodName.length() - shortMethodNameSuffix.length());
        }
        return methodName;
    }

    /**
     * 获取目标方法的参数，去除参数中的敏感字段
     *
     * @param joinPoint 连接点
     * @return
     */
    private String getParamsJson(ProceedingJoinPoint joinPoint) {
        // 获取参数数组
        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        // 循环参数数组
        for(Object arg : args) {
            // 移除敏感内容
            String paramStr;
            if (arg instanceof HttpServletResponse) {
                paramStr = HttpServletResponse.class.getSimpleName();
            } else if (arg instanceof HttpServletRequest) {
                paramStr = HttpServletRequest.class.getSimpleName();
            } else if (arg instanceof MultipartFile) {
                long size = ((MultipartFile) arg).getSize();
                paramStr = MultipartFile.class.getSimpleName() + " size:" + size;
            } else {
                paramStr = this.deleteSensitiveContent(arg);
            }
            sb.append(paramStr).append(",");
        }
        // 移除最后一个逗号
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * 相关的控制器中的方法是否需要记录日志
     *
     * @param method
     * @return
     */
    private boolean needToLog(Method method) {
        // GET请求和全局异常处理器注解标记的方法不记录日志
        return method.getAnnotation(GetMapping.class) == null
                && !method.getDeclaringClass().equals(GlobalExceptionHandler.class);
    }

    /**
     * 删除参数中的敏感内容
     *
     * @param obj 参数对象
     * @return 去除敏感内容后的参数对象
     */
    private String deleteSensitiveContent(Object obj) {
        JSONObject jsonObject = new JSONObject();
        if (obj == null || obj instanceof Exception) {
            return jsonObject.toJSONString();
        }

        try {
            String param = JSON.toJSONString(obj);
            jsonObject = JSONObject.parseObject(param);
            // 获取密码等关键字列表，移除这些敏感字段
            List<String> sensitiveFieldList = this.getSensitiveFieldList();
            for(String sensitiveField : sensitiveFieldList) {
                if (jsonObject.containsKey(sensitiveField)) {
                    // 把这些敏感字段使用*代替
                    jsonObject.put(sensitiveField, "******");
                }
            }
        } catch (ClassCastException e) {
            return String.valueOf(obj);
        }
        return jsonObject.toJSONString();
    }

    /**
     * 敏感字段列表-密码相关的关键字
     */
    private List<String> getSensitiveFieldList() {
        List<String> sensitiveFieldList = Lists.newArrayList();
        sensitiveFieldList.add("pwd");
        sensitiveFieldList.add("password");
        return sensitiveFieldList;
    }
}
