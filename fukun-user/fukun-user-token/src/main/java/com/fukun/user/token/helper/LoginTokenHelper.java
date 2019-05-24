package com.fukun.user.token.helper;

import com.fukun.commons.annotations.LoginAuth;
import com.fukun.commons.util.CookieUtil;
import com.fukun.commons.util.RequestContextUtil;
import com.fukun.commons.util.StringUtil;
import com.fukun.user.model.bo.LoginToken;
import com.fukun.user.model.bo.LoginUser;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 登录TOKEN辅助类
 *
 * @author tangyifei
 * @since 2019-5-24 10:41:31
 */
public class LoginTokenHelper {

    private final static String SECRET_KEY = "Ld4Dl5f9OoYTezPK";

    private final static String LOGIN_TOKEN_COOKIE_NAME = "X-Token";

    private final static String LOGIN_TOKEN_KEY = "LOGIN-TOKEN";

    /**
     * 根据登录的相关信息生成TOKEN ID
     *
     * @param loginAccount 登录账号
     * @param accountType  账号类型
     * @param ip           登录IP
     * @param platform     登录平台
     * @param loginTime    登录时间
     * @param ttl          生存时长(单位：秒)
     * @return
     */
    public static String generateId(String loginAccount, String accountType,
                                    String ip, String platform, Date loginTime, long ttl) {
        StringBuilder noEncodeLoginTokenId = new StringBuilder(loginAccount)
                .append(accountType)
                .append(ip)
                .append(platform)
                .append(loginTime)
                .append(ttl);

        return DigestUtils.sha256Hex(SECRET_KEY + DigestUtils.md5Hex(noEncodeLoginTokenId.toString()) + DigestUtils.md5Hex(SECRET_KEY));
    }

    /**
     * 添加登录TOKEN的ID信息到COOKIE中
     *
     * @param loginTokenId   登录的taken id
     * @param expiredTimeSec 生存时长
     */
    public static void addLoginTokenIdToCookie(String loginTokenId, Integer expiredTimeSec) {
        HttpServletResponse response = RequestContextUtil.getResponse();
        CookieUtil.addCookie(response, LOGIN_TOKEN_COOKIE_NAME, loginTokenId, expiredTimeSec == null ? -1 : expiredTimeSec, true);
    }

    /**
     * 从COOKIE中清理登录账号信息
     */
    public static void delLoginTokenIdFromCookie() {
        HttpServletRequest request = RequestContextUtil.getRequest();
        HttpServletResponse response = RequestContextUtil.getResponse();

        CookieUtil.delCookie(request, response, LOGIN_TOKEN_COOKIE_NAME);
    }

    /**
     * 从请求头中或者从浏览器的cookie中获取登录的TOKEN的ID
     *
     * @return 登录token
     */
    public static String getLoginTokenId() {
        HttpServletRequest request = RequestContextUtil.getRequest();
        String token = request.getHeader(LOGIN_TOKEN_COOKIE_NAME);
        if (StringUtil.isEmpty(token)) {
            token = CookieUtil.getCookieValue(request, LOGIN_TOKEN_COOKIE_NAME, true);
        }
        return token;
    }

    /**
     * 将登录TOKEN信息放入请求对象中
     *
     * @param loginToken 登录的token信息
     */
    public static void addLoginTokenToRequest(LoginToken loginToken) {
        RequestContextUtil.getRequest().setAttribute(LOGIN_TOKEN_KEY, loginToken);
    }

    /**
     * 从请求对象中获取登录用户信息
     * 备注：使用该方法时需要在对应controller类或方法上加{@link LoginAuth}}注解
     *
     * @return
     */
    public static LoginUser getLoginUserFromRequest() {
        LoginToken loginToken = getLoginTokenFromRequest();
        if (loginToken == null) {
            return null;
        }

        return loginToken.getLoginUser();
    }

    /**
     * 获取登录TOKEN信息从请求对象
     * 备注：使用该方法时需要在对应controller类或方法上加{@link LoginAuth}}注解
     *
     * @return 登录的token信息
     */
    public static LoginToken getLoginTokenFromRequest() {
        Object loginTokenO = RequestContextUtil.getRequest().getAttribute(LOGIN_TOKEN_KEY);
        if (loginTokenO == null) {
            return null;
        }

        return (LoginToken) loginTokenO;
    }

}
