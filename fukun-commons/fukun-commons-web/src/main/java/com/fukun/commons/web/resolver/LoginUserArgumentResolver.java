package com.fukun.commons.web.resolver;

import com.fukun.commons.annotations.LoginAuth;
import com.fukun.user.model.bo.LoginUser;
import com.fukun.user.token.helper.LoginTokenHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;

/**
 * 登录用户参数解析器
 *
 * @author tangyifei
 * @since 2019-5-24 11:02:19
 */
@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        final Method method = parameter.getMethod();
        final Class<?> clazz = parameter.getMethod().getDeclaringClass();

        boolean isHasLoginAuthAnn = clazz.isAnnotationPresent(LoginAuth.class) || method.isAnnotationPresent(LoginAuth.class);
        boolean isHasLoginUserParameter = parameter.getParameterType().isAssignableFrom(LoginUser.class);

        return isHasLoginAuthAnn && isHasLoginUserParameter;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return LoginTokenHelper.getLoginUserFromRequest();
    }
}
