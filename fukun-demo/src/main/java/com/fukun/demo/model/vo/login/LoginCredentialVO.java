package com.fukun.demo.model.vo.login;

import com.fukun.commons.model.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录凭证VO
 *
 * @author tangyifei
 * @since 2019-5-24 15:20:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginCredentialVO implements Model {

    private static final long serialVersionUID = 5550420394013305835L;

    @ApiModelProperty(value = "凭证ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "账号", example = "15062230055")
    private String account;

    @ApiModelProperty(value = "账号类型", example = "1")
    private String type;

}
