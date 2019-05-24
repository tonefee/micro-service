package com.fukun.demo.web.demo3;

import com.fukun.commons.enums.ResultCode;
import com.fukun.commons.exceptions.BusinessException;
import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.demo.helper.PasswordHelper;
import com.fukun.demo.model.qo.login.LoginCredentialQO;
import com.fukun.user.client.LoginCredentialClient;
import com.fukun.user.model.po.LoginCredential;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登录凭证控制器（用户登录凭证就是用来存储用的登录账号和密码的地方，以后集成微信、新浪等第三方平台的账号都会存在此表中）
 *
 * @author tangyifei
 * @since 2019-5-24 16:01:03
 */
@Api(value = "登录凭证管理", description = "登录凭证管理")
@ResponseResult
@RestController("demo3LoginCredentialController")
@RequestMapping("demo3/login-credentials")
public class LoginCredentialController {

    @Resource
    private LoginCredentialClient loginCredentialClient;

    @ApiOperation("添加登录凭证")
    @PostMapping
    public LoginCredential add(@RequestBody @Validated LoginCredentialQO loginCredentialQO) {

        LoginCredential dbLoginCredential = loginCredentialClient.getLoginCredential(loginCredentialQO.getAccount(), loginCredentialQO.getType());
        if (dbLoginCredential != null) {
            throw new BusinessException(ResultCode.LOGIN_CREDENTIAL_EXISTED);
        }

        String randomSalt = PasswordHelper.generateRandomSalt();
        String encodePwd = PasswordHelper.encodeBySalt(loginCredentialQO.getPwd(), randomSalt);

        LoginCredential loginCredential = com.fukun.user.model.po.LoginCredential.builder()
                .account(loginCredentialQO.getAccount())
                .pwd(encodePwd)
                .randomSalt(randomSalt)
                .type(loginCredentialQO.getType())
                .userId(loginCredentialQO.getUserId())
                .build();

        return loginCredentialClient.add(loginCredential);
    }

}
