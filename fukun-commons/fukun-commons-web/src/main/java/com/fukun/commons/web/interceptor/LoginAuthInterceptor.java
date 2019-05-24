package com.fukun.commons.web.interceptor;

import com.fukun.commons.annotations.LoginAuth;
import com.fukun.commons.enums.ResultCode;
import com.fukun.commons.exceptions.BusinessException;
import com.fukun.commons.util.StringUtil;
import com.fukun.user.model.bo.LoginToken;
import com.fukun.user.model.bo.LoginUser;
import com.fukun.user.token.helper.LoginTokenHelper;
import com.fukun.user.token.service.LoginTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 已登录权限验证拦截器 备注：通过{@link LoginAuth}配合使用
 *
 * @author tangyifei
 * @since 2019-5-24 11:00:46
 */
public class LoginAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTokenService loginTokenCacheService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();

            if (clazz.isAnnotationPresent(LoginAuth.class) || method.isAnnotationPresent(LoginAuth.class)) {

                //直接获取登录用户（防止请求转发时，第二次查询）
                LoginUser loginUser = LoginTokenHelper.getLoginUserFromRequest();
                if (loginUser != null) {
                    return true;
                }

                //获取登录TOKEN ID
                String loginTokenId = LoginTokenHelper.getLoginTokenId();
                if (StringUtil.isEmpty(loginTokenId)) {
                    throw new BusinessException(ResultCode.USER_NOT_LOGGED_IN);
                }

                //获取登录TOKEN信息
                LoginToken loginToken = loginTokenCacheService.getById(loginTokenId);
                if (loginToken == null) {
                    throw new BusinessException(ResultCode.USER_NOT_LOGGED_IN);
                }

                //登录TOKEN信息放入请求对象，方便后续controller中获取
                LoginTokenHelper.addLoginTokenToRequest(loginToken);
                return true;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // nothing to do
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // nothing to do
    }

}
