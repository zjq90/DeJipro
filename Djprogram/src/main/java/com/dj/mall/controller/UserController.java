package com.dj.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Result;
import com.dj.mall.context.UserContext;
import com.dj.mall.entity.SysUser;
import com.dj.mall.entity.UserAuth;
import com.dj.mall.entity.UserLevel;
import com.dj.mall.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "用户中心接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo() {
        Long userId = UserContext.getUserId();
        Map<String, Object> info = userService.getUserInfo(userId);
        return Result.success(info);
    }

    @ApiOperation("更新用户基本信息")
    @PutMapping("/update")
    public Result<Void> updateUser(@RequestBody SysUser user) {
        Long userId = UserContext.getUserId();
        user.setId(userId);
        user.setPassword(null);
        user.setStatus(null);
        user.setInviteCode(null);
        user.setParentId(null);
        user.setLevelId(null);
        userService.updateById(user);
        return Result.success();
    }

    @ApiOperation("获取实名认证信息")
    @GetMapping("/auth")
    public Result<UserAuth> getUserAuth() {
        Long userId = UserContext.getUserId();
        UserAuth auth = userService.getUserAuth(userId);
        return Result.success(auth);
    }

    @ApiOperation("提交实名认证")
    @PostMapping("/auth")
    public Result<Void> submitAuth(@RequestBody UserAuth auth) {
        Long userId = UserContext.getUserId();
        auth.setUserId(userId);
        userService.submitAuth(auth);
        return Result.success();
    }

    @ApiOperation("获取所有会员等级")
    @GetMapping("/levels")
    public Result<List<UserLevel>> getAllLevels() {
        List<UserLevel> levels = userService.getAllLevels();
        return Result.success(levels);
    }

    @ApiOperation("获取我的团队列表")
    @GetMapping("/team")
    public Result<Page<SysUser>> getTeamList(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserContext.getUserId();
        Page<SysUser> page = userService.getTeamList(userId, pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("修改密码")
    @PostMapping("/password")
    public Result<Void> updatePassword(
            @ApiParam(value = "原密码", required = true) @RequestParam String oldPassword,
            @ApiParam(value = "新密码", required = true) @RequestParam String newPassword) {
        Long userId = UserContext.getUserId();
        userService.updatePassword(userId, oldPassword, newPassword);
        return Result.success();
    }

    @ApiOperation("更换手机号")
    @PostMapping("/phone")
    public Result<Void> updatePhone(
            @ApiParam(value = "新手机号", required = true) @RequestParam String phone,
            @ApiParam(value = "验证码", required = true) @RequestParam String code) {
        Long userId = UserContext.getUserId();
        userService.updatePhone(userId, phone, code);
        return Result.success();
    }
}
