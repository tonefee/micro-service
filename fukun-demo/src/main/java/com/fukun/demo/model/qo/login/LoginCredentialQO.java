package com.fukun.demo.model.qo.login;

import com.fukun.commons.annotations.EnumValue;
import com.fukun.user.model.po.LoginCredential;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 登录凭证
 *
 * @author tangyifei
 * @since 2019-5-24 15:13:13
 */
@ApiModel("凭证QO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginCredentialQO {

    @ApiModelProperty(value = "账号", example = "15062230055")
    @NotBlank
    @Length(min = 1, max = 128)
    private String account;

    @ApiModelProperty(value = "密码", example = "123456")
    private String pwd;

    @ApiModelProperty(value = "用户ID", example = "1")
    @NotBlank
    private String userId;

    @ApiModelProperty(value = "账号类型", example = "1")
    @EnumValue(enumClass = LoginCredential.TypeEnum.class, enumMethod = "isValidName")
    private String type;

}
