package com.fukun.user.token.service.impl;

import com.fukun.commons.enums.CacheKeyEnum;
import com.fukun.user.model.bo.LoginToken;
import com.fukun.user.token.helper.LoginTokenHelper;
import com.fukun.user.token.service.LoginTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * 用户登录TOKEN服务实现类
 *
 * @author tangyifei
 * @since 2019-5-24 10:42:32
 */
@Slf4j
public class LoginTokenCacheServiceImpl implements LoginTokenService {

    private ValueOperations<String, LoginToken> loginTokenValueOps;

    private RedisTemplate<String, LoginToken> loginTokenTemplate;

    private String loginTokenCacheKeyPrefix;

    public LoginTokenCacheServiceImpl(RedisTemplate<String, LoginToken> loginTokenTemplate, String loginTokenCacheKeyPrefix) {
        Assert.notNull(loginTokenTemplate, "loginTokenTemplate is not null.");
        Assert.notNull(loginTokenCacheKeyPrefix, "loginTokenCacheKeyPrefix is not null.");

        this.loginTokenTemplate = loginTokenTemplate;
        this.loginTokenCacheKeyPrefix = loginTokenCacheKeyPrefix;
        this.loginTokenValueOps = loginTokenTemplate.opsForValue();
    }

    private String getLoginTokenCacheKey(String token) {
        return loginTokenCacheKeyPrefix + token;
    }

    /**
     * 将loginToken放到缓存中
     *
     * @param loginToken the login token
     * @return
     */
    @Override
    public LoginToken add(LoginToken loginToken) {
        Assert.notNull(loginToken, "loginToken is not null");
        Assert.notNull(loginToken.getLoginUser(), "loginToken.getLoginUser() is not null");
        Assert.notNull(loginToken.getLoginCredential(), "loginToken.getLoginCredential() is not null");

        String token = LoginTokenHelper.generateId(loginToken.getLoginCredential().getAccount(), loginToken.getLoginCredential().getType(), loginToken.getIp(), loginToken.getPlatform(), loginToken.getCreateTime(), loginToken.getTtl());
        loginToken.setId(token);
        loginTokenValueOps.set(this.getLoginTokenCacheKey(loginToken.getId()), loginToken, CacheKeyEnum.VALUE_LOGIN_TOKENS.sec(), TimeUnit.SECONDS);
        return loginToken;
    }

    /**
     * 从缓存中删除相关的key对应的值
     *
     * @param id tokenId
     */
    @Override
    public void deleteById(String id) {
        Assert.notNull(id, "id is not null");

        loginTokenTemplate.delete(this.getLoginTokenCacheKey(id));
    }

    /**
     * 从缓存中根据相关的key获取key对应的值
     *
     * @param id tokenId
     * @return
     */
    @Override
    public LoginToken getById(String id) {
        Assert.notNull(id, "id is not null");

        return loginTokenValueOps.get(this.getLoginTokenCacheKey(id));
    }

    /**
     * 获取生存时长
     *
     * @param id the id
     * @return
     */
    @Override
    public long ttl(String id) {
        Assert.notNull(id, "id is not null");

        return loginTokenTemplate.getExpire(this.getLoginTokenCacheKey(id), TimeUnit.SECONDS);
    }

}
