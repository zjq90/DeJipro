package com.dj.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Result;
import com.dj.mall.entity.PromotionPoster;
import com.dj.mall.entity.PromotionStat;
import com.dj.mall.service.PromotionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "推广管理接口")
@RestController
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @ApiOperation("获取推广海报列表")
    @GetMapping("/poster/list")
    public Result<List<PromotionPoster>> getPosterList(
            @ApiParam(value = "海报类型: 1推广海报 2邀新海报") @RequestParam(required = false) Integer posterType) {
        List<PromotionPoster> list = promotionService.getPosterList(posterType);
        return Result.success(list);
    }

    @ApiOperation("获取推广海报详情")
    @GetMapping("/poster/detail/{id}")
    public Result<PromotionPoster> getPosterDetail(@PathVariable Long id) {
        PromotionPoster poster = promotionService.getPosterById(id);
        return Result.success(poster);
    }

    @ApiOperation("获取推广统计数据列表")
    @GetMapping("/stat/list")
    public Result<Page<PromotionStat>> getPromotionStatList(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<PromotionStat> page = promotionService.getPromotionStatPage(userId, pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("获取推广数据汇总")
    @GetMapping("/stat/summary")
    public Result<Map<String, Object>> getPromotionSummary(
            @ApiParam(value = "用户ID") @RequestParam Long userId) {
        Map<String, Object> summary = promotionService.getPromotionSummary(userId);
        return Result.success(summary);
    }

    @ApiOperation("获取推广人排行榜")
    @GetMapping("/rank")
    public Result<Page<Map<String, Object>>> getPromotionRank(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Map<String, Object>> page = promotionService.getPromotionRank(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("获取推广人列表")
    @GetMapping("/users")
    public Result<Page<Map<String, Object>>> getPromotionUserList(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Map<String, Object>> page = promotionService.getPromotionUserList(userId, pageNum, pageSize);
        return Result.success(page);
    }
}
