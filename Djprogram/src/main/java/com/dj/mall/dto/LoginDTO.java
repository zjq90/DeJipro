package com.dj.mall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "登录请求参数")
public class LoginDTO {

    @ApiModelProperty(value = "登录类型: 1密码登录 2手机号密码登录 3验证码登录", required = true)
    @NotNull(message = "登录类型不能为空")
    private Integer loginType;

    @ApiModelProperty(value = "用户名/手机号", required = true)
    @NotBlank(message = "账号不能为空")
    private String account;

    @ApiModelProperty(value = "密码/验证码", required = true)
    private String password;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "登录IP")
    private String loginIp;
}
