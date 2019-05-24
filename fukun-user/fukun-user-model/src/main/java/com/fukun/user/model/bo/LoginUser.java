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

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "登陆账号")
    private String nickname;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "最新登陆IP")
    private String latestLoginIp;

    @ApiModelProperty(value = "最新登陆时间")
    private Date latestLoginTime;

}
