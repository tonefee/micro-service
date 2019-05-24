package com.fukun.demo.web.demo3;

import com.fukun.commons.annotations.LoginAuth;
import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.user.model.bo.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Spring自定参数解析器之《自动注入已登录用户》
 *
 * @author tangyifie
 * @since 2019-5-24 16:11:22
 */
@Api(value = "我的账户", description = "我的账户")
@ResponseResult
@RestController("demo3MyAccountController")
@RequestMapping("demo3/my-account")
public class MyAccountController {

    @ApiOperation("查询我的画像")
    @LoginAuth
    @GetMapping("profile")
    public LoginUser myAccount(@ApiIgnore LoginUser loginUser) {
        return loginUser;
    }

}
