package com.fukun.demo.service.login.impl;

import com.fukun.commons.enums.CacheKeyEnum;
import com.fukun.commons.enums.ResultCode;
import com.fukun.commons.exceptions.BusinessException;
import com.fukun.commons.util.BeanUtil;
import com.fukun.commons.util.IpUtil;
import com.fukun.commons.util.RequestContextUtil;
import com.fukun.commons.web.constants.HeaderConstants;
import com.fukun.demo.helper.PasswordHelper;
import com.fukun.demo.model.qo.login.LoginQO;
import com.fukun.demo.model.vo.login.LoginCredentialVO;
import com.fukun.demo.model.vo.login.LoginVO;
import com.fukun.demo.service.login.LoginService;
import com.fukun.user.client.LoginCredentialClient;
import com.fukun.user.client.UserClient;
import com.fukun.user.model.bo.LoginToken;
import com.fukun.user.model.bo.LoginUser;
import com.fukun.user.model.po.LoginCredential;
import com.fukun.user.model.po.User;
import com.fukun.user.token.helper.LoginTokenHelper;
import com.fukun.user.token.service.LoginTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 登录服务实现
 *
 * @author tangyifei
 * @since 2019-5-24 15:25:17
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginTokenService loginTokenService;

    @Resource
    private LoginCredentialClient loginCredentialClient;

    @Resource
    private UserClient userClient;

    @Override
    public LoginVO login(LoginQO loginQO) {
        // 根据用户的账号和凭证类型获取该用户登录的凭证列表
        List<LoginCredential> loginCredentialList = loginCredentialClient.getLoginCredentialList(loginQO.getAccount(), loginQO.getType());
        if (loginCredentialList.size() == 0) {
            log.info("login account is nonexistent, account:{}", loginQO.getAccount());
            throw new BusinessException(ResultCode.USER_LOGIN_ERROR);
        }

        //验证密码是否正确
        LoginCredential firstLoginCredential = loginCredentialList.get(0);
        if (!firstLoginCredential.getPwd().equals(PasswordHelper.encodeBySalt(loginQO.getPwd(), firstLoginCredential.getRandomSalt()))) {
            log.info("login account' password is error");
            throw new BusinessException(ResultCode.USER_LOGIN_ERROR);
        }

        User user = userClient.getById(firstLoginCredential.getUserId());
        if (user == null) {
            log.info("login user is null");
            throw new BusinessException(ResultCode.USER_LOGIN_ERROR);
        }

        LoginToken loginToken = this.saveLoginToken(user, firstLoginCredential);

        LoginUser loginUser = new LoginUser();
        BeanUtil.copyProperties(user, loginUser);

        LoginCredentialVO loginCredential = new LoginCredentialVO();
        BeanUtil.copyProperties(firstLoginCredential, loginCredential);

        return LoginVO.builder()
                .token(loginToken.getId())
                .loginTime(loginToken.getCreateTime())
                .ip(loginToken.getIp())
                .platform(loginToken.getPlatform())
                .ttl(loginToken.getTtl())
                .user(loginUser)
                .loginCredential(loginCredential)
                .build();
    }

    private LoginToken saveLoginToken(User user, LoginCredential loginCredential) {
        Date currentDate = new Date();
        LoginUser loginUser = new LoginUser();
        BeanUtil.copyProperties(user, loginUser);

        HttpServletRequest request = RequestContextUtil.getRequest();

        LoginToken loginToken = LoginToken.builder()
                .createTime(currentDate)
                .ip(IpUtil.getRealIp(request))
                .platform(request.getHeader(HeaderConstants.CALL_SOURCE))
                .ttl(CacheKeyEnum.VALUE_LOGIN_TOKENS.sec().longValue())
                .loginCredential(loginCredential)
                .loginUser(loginUser)
                .build();

        loginToken = loginTokenService.add(loginToken);
        LoginTokenHelper.addLoginTokenIdToCookie(loginToken.getId(), CacheKeyEnum.VALUE_LOGIN_TOKENS.sec());
        return loginToken;
    }

    @Override
    public void logout() {
        LoginToken loginToken = LoginTokenHelper.getLoginTokenFromRequest();
        if (loginToken == null) {
            throw new BusinessException(ResultCode.USER_NOT_LOGGED_IN);
        }

        loginTokenService.deleteById(loginToken.getId());
        LoginTokenHelper.delLoginTokenIdFromCookie();
    }
}
