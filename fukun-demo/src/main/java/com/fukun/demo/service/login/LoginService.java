package com.fukun.demo.service.login;

import com.fukun.demo.model.qo.login.LoginQO;
import com.fukun.demo.model.vo.login.LoginVO;

/**
 * 登录服务
 *
 * @author tangyifei
 * @since 2019-5-24 15:23:37
 */
public interface LoginService {

    /**
     * 用户登录
     *
     * @param loginQO 登录查询对象
     * @return 登录视图对象
     */
    LoginVO login(LoginQO loginQO);

    /**
     * 用户登出
     */
    void logout();
}
