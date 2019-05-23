package com.fukun.commons.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * cookie操作工具类
 *
 * @author tangyifei
 * @since 2019-5-22 15:54:29 PM
 */
@Slf4j
public class CookieUtil {

    /**
     * 添加cookie信息到浏览器中，不设置生效时间，默认浏览器关闭即失效 备注：默认开启HttpOnly
     *
     * @param response 响应
     * @param name     名称
     * @param value    值
     */
    public static void addCookie(HttpServletResponse response, String name, String value) {
        addCookie(response, name, value, -1, false, true, false);
    }

    /**
     * 添加cookie信息到浏览器中,指定生效时间
     * <p>secure属性</p>
     * <p>
     * 当设置为true时,表示创建的 Cookie 会被以安全的形式向服务器传输,<br/>
     * 也就是只能在 HTTPS 连接中被浏览器传递到服务器端进行会话验证,<br/>
     * 如果是 HTTP 连接则不会传递该信息，所以不会被窃取到Cookie 的具体内容.<br/>
     * </p>
     * <p>HttpOnly属性</p>
     * <p>
     * 如果在Cookie中设置了"HttpOnly"属性,<br/>
     * 那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息,这样能有效的防止XSS攻击.
     * </p>
     *
     * @param response    响应
     * @param name        名称
     * @param value       值
     * @param maxAge      最大生存时长
     * @param isURLEncode 是否需要进行编码
     * @param isHttpOnly  是否仅仅是httpOnly
     * @param isSecure    是否开启安全传输
     */
    public static void addCookie(HttpServletResponse response, String name, String value,
                                 int maxAge, boolean isURLEncode, boolean isHttpOnly,
                                 boolean isSecure) {
        try {
            Cookie cookie = new Cookie(name, isURLEncode ? URLEncoder.encode(value, "UTF-8") : value);
            if (maxAge > 0) {
                cookie.setMaxAge(maxAge);
            }

            cookie.setPath("/");
            cookie.setHttpOnly(isHttpOnly);
            cookie.setSecure(isSecure);
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            log.error("addCookie occurs exception, caused by: ", e);
        }
    }

    /**
     * 删除cookie
     *
     * @param request  请求
     * @param response 响应
     * @param name     名称
     */
    public static void delCookie(HttpServletRequest request, HttpServletResponse response,
                                 String name) {
        if (StringUtil.isEmpty(name)) {
            return;
        }

        Cookie cookie = getCookie(request, name);
        if (null != cookie) {
            cookie.setPath("/");
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 获取cookie对象
     *
     * @param request 请求
     * @param name    名称
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }

        return null;
    }

    /**
     * 通过cookie名称获取登陆的cookie值
     *
     * @param request 请求
     * @param name    名称
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    /**
     * 添加cookie信息，指定生效时间，是否URL编码 备注：默认开启HttpOnly
     *
     * @param response    响应
     * @param name        名称
     * @param value       值
     * @param maxAge      最大生效时间
     * @param isURLEncode 是否进行uri编码
     */
    public static void addCookie(HttpServletResponse response, String name,
                                 String value, int maxAge, boolean isURLEncode) {
        if (StringUtil.isEmpty(value)) {
            return;
        }

        addCookie(response, name, value, maxAge, isURLEncode, true, false);
    }

    /**
     * 获取登录的cookie值，是否进行解码
     *
     * @param request  请求
     * @param name     cookie名称
     * @param isDecode 是否进行编码
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name, boolean isDecode) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }

        try {
            Cookie cookie = getCookie(request, name);
            if (cookie != null) {
                return isDecode ? URLDecoder.decode(cookie.getValue(), "UTF-8") : cookie.getValue();
            }
        } catch (UnsupportedEncodingException e) {
            log.error("getCookieValue occurs exception, caused by: ", e);
        }
        return null;
    }
}
