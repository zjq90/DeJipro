package com.dj.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Result;
import com.dj.mall.entity.SubscribeTemplate;
import com.dj.mall.entity.UserSubscribe;
import com.dj.mall.service.SubscribeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "订阅消息接口")
@RestController
@RequestMapping("/subscribe")
public class SubscribeController {

    @Autowired
    private SubscribeService subscribeService;

    @ApiOperation("获取订阅消息模板列表")
    @GetMapping("/template/list")
    public Result<List<SubscribeTemplate>> getTemplateList() {
        List<SubscribeTemplate> list = subscribeService.getTemplateList();
        return Result.success(list);
    }

    @ApiOperation("获取订阅消息模板分页列表")
    @GetMapping("/template/page")
    public Result<Page<SubscribeTemplate>> getTemplatePage(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<SubscribeTemplate> page = subscribeService.getTemplatePage(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("获取订阅消息模板详情")
    @GetMapping("/template/detail/{id}")
    public Result<SubscribeTemplate> getTemplateDetail(@PathVariable Long id) {
        SubscribeTemplate template = subscribeService.getTemplateById(id);
        return Result.success(template);
    }

    @ApiOperation("订阅消息")
    @PostMapping("/subscribe")
    public Result<UserSubscribe> subscribe(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "模板ID") @RequestParam Long templateId) {
        UserSubscribe userSubscribe = subscribeService.subscribe(userId, templateId);
        return Result.success(userSubscribe);
    }

    @ApiOperation("获取用户订阅列表")
    @GetMapping("/my/list")
    public Result<List<UserSubscribe>> getUserSubscribes(
            @ApiParam(value = "用户ID") @RequestParam Long userId) {
        List<UserSubscribe> list = subscribeService.getUserSubscribes(userId);
        return Result.success(list);
    }
}
