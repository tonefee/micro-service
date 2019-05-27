package com.fukun.demo.web.demo3;

import com.fukun.commons.annotations.LoginAuth;
import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.demo.model.qo.login.LoginQO;
import com.fukun.demo.model.vo.login.LoginVO;
import com.fukun.demo.service.login.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登陆接口
 *
 * @author tangyifei
 * @date 2019-5-24 15:59:12
 * @since 2019-5-24 15:59:06
 */
@Api(value = "登陆管理", description = "登陆/登出")
@Slf4j
@ResponseResult
@RestController("demo3LoginController")
@RequestMapping("demo3")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "员工登陆", response = LoginVO.class, notes = "员工登陆使用登陆账号和密码进行登陆", httpMethod = "POST")
    @PostMapping("/login")
    public LoginVO login(@Validated @RequestBody LoginQO loginQO) {
        LoginVO loginVO = loginService.login(loginQO);
        return loginVO;
    }


    @ApiOperation(value = "退出登陆")
    @LoginAuth
    @PostMapping(value = "/logout")
    public void logout() {
        loginService.logout();
    }

}
