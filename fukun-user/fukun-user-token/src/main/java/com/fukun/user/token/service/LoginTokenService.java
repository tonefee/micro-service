package com.fukun.user.token.service;


import com.fukun.user.model.bo.LoginToken;

/**
 * 用户登录TOKEN服务
 *
 * @author tangyifei
 * @since 2019-5-24 10:43:30
 */
public interface LoginTokenService {

    /**
     * Add login token.
     *
     * @param loginToken the login token
     * @return the login token
     */
    LoginToken add(LoginToken loginToken);

    /**
     * Delete by id.
     *
     * @param id the id
     */
    void deleteById(String id);

    /**
     * Get by id login token.
     *
     * @param id the id
     * @return the login token
     */

    LoginToken getById(String id);

    /**
     * Ttl long.
     *
     * @param id the id
     * @return the long
     */
    long ttl(String id);

}
