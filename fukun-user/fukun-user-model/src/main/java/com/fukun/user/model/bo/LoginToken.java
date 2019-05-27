package com.fukun.user.model.bo;

import com.fukun.commons.enums.CallSourceEnum;
import com.fukun.user.model.po.LoginCredential;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * 登录的TOKEN业务对象
 *
 * @author tangyifei
 * @since 2019-5-24 09:34:25
 */
@ApiModel("登录的TOKEN业务对象")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginToken {

    @ApiModelProperty(value = "登陆token ID", required = true, position = 0, example = "1")
    private String id;

    @ApiModelProperty(value = "生存时长(单位：秒)", required = true, position = 1, example = "3600")
    private Long ttl;

    @ApiModelProperty(value = "登录IP", required = true, position = 2, example = "192168023")
    private String ip;

    /**
     * 平台，调用来源 {@link CallSourceEnum}
     */
    @ApiModelProperty(value = "登录平台", required = true, position = 3, example = "1")
    private String platform;

    @ApiModelProperty(value = "登录时间", required = true, position = 4, example = "20190527")
    private Date createTime;

    @ApiModelProperty(value = "登录凭证", required = true, position = 5, example = "131331141489898900")
    LoginCredential loginCredential;

    @ApiModelProperty(value = "登录的用户信息", required = true, position = 6, example = "1")
    private LoginUser loginUser;

}
