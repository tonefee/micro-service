package com.fukun.user.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录的用户的业务对象
 *
 * @author tangyifei
 * @since 2019-5-24 09:37:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements Serializable {

    private static final long serialVersionUID = -3675314128118074922L;

    @ApiModelProperty(value = "用户ID", example = "1")
    private String id;

    @ApiModelProperty(value = "登陆账号", example = "1")
    private String nickname;

    @ApiModelProperty(value = "性别", example = "1")
    private String gender;

    @ApiModelProperty(value = "头像", example = "1")
    private String avatar;

    @ApiModelProperty(value = "类型", example = "1")
    private String type;

    @ApiModelProperty(value = "最新登陆IP", example = "1")
    private String latestLoginIp;

    @ApiModelProperty(value = "最新登陆时间", example = "1")
    private Date latestLoginTime;

}
