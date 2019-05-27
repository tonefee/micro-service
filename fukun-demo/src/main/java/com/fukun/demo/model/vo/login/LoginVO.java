package com.fukun.demo.model.vo.login;

import com.fukun.commons.model.Model;
import com.fukun.user.model.bo.LoginUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 登录VO
 *
 * @author tangyifei
 * @since 2019-5-24 15:21:52
 */
@ApiModel("登录VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO implements Model {

    private static final long serialVersionUID = -9111387775700628962L;

    @ApiModelProperty(value = "用户登陆TOKEN", example = "11211442146788")
    private String token;

    @ApiModelProperty(value = "过期时间（单位：秒）", example = "3600")
    private Long ttl;

    @ApiModelProperty(value = "登陆IP", example = "192168053")
    private String ip;

    @ApiModelProperty(value = "登陆平台", example = "1")
    private String platform;

    @ApiModelProperty(value = "登陆时间", example = "20190527")
    private Date loginTime;

    @ApiModelProperty(value = "用户信息", example = "1")
    private LoginUser user;

    @ApiModelProperty(value = "凭证", example = "1")
    private LoginCredentialVO loginCredential;
}
