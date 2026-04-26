package com.dj.mall.controller;

import com.dj.mall.common.Result;
import com.dj.mall.context.UserContext;
import com.dj.mall.dto.LoginDTO;
import com.dj.mall.dto.RegisterDTO;
import com.dj.mall.service.AuthService;
import com.dj.mall.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "认证接口")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO dto, HttpServletRequest request) {
        dto.setLoginIp(getClientIp(request));
        LoginVO vo = authService.login(dto);
        return Result.success(vo);
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public Result<LoginVO> register(@Validated @RequestBody RegisterDTO dto) {
        LoginVO vo = authService.register(dto);
        return Result.success(vo);
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        Long userId = UserContext.getUserId();
        authService.logout(userId);
        return Result.success();
    }

    @ApiOperation("发送验证码")
    @PostMapping("/sms-code")
    public Result<Void> sendSmsCode(
            @ApiParam(value = "手机号", required = true) @RequestParam String phone,
            @ApiParam(value = "类型: 1注册 2登录 3重置密码 4绑定手机", required = true) @RequestParam Integer type) {
        authService.sendSmsCode(phone, type);
        return Result.success();
    }

    @ApiOperation("重置密码")
    @PostMapping("/reset-password")
    public Result<Void> resetPassword(
            @ApiParam(value = "手机号", required = true) @RequestParam String phone,
            @ApiParam(value = "验证码", required = true) @RequestParam String code,
            @ApiParam(value = "新密码", required = true) @RequestParam String newPassword) {
        authService.resetPassword(phone, code, newPassword);
        return Result.success();
    }

    @ApiOperation("注销账号")
    @PostMapping("/cancel")
    public Result<Void> cancelAccount(
            @ApiParam(value = "注销原因") @RequestParam(required = false) String reason) {
        Long userId = UserContext.getUserId();
        authService.cancelAccount(userId, reason);
        return Result.success();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
