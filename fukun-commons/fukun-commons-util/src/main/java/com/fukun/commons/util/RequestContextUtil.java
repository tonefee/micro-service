package com.fukun.commons.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 应用级对象获取工具类
 *
 * @author tangyifei
 * @since 2019-5-22 16:26:10 PM
 */
public class RequestContextUtil implements ApplicationContextAware {

    /**
     * 创建应用程序上下文的引用
     */
    private static ApplicationContext applicationContext;

    /**
     * 获取应用上下文的请求对象
     *
     * @return 请求对象
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取应用上下文的响应对象
     *
     * @return 响应对象
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取应用上下文的会话对象
     *
     * @return 会话对象
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }

    public static ServletContext getServletContext() {
        return ContextLoader.getCurrentWebApplicationContext().getServletContext();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RequestContextUtil.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return (T) applicationContext.getBeansOfType(clazz);
    }

    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
        }
    }

}
