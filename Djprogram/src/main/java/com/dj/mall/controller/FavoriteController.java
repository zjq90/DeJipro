package com.dj.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Result;
import com.dj.mall.context.UserContext;
import com.dj.mall.service.FavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "收藏管理接口")
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @ApiOperation("获取收藏列表")
    @GetMapping("/list")
    public Result<Page<Map<String, Object>>> getFavoriteList(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = UserContext.getUserId();
        Page<Map<String, Object>> page = favoriteService.getFavoriteList(userId, pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("添加收藏")
    @PostMapping("/add")
    public Result<Void> addFavorite(
            @ApiParam(value = "商品ID", required = true) @RequestParam Long goodsId) {
        Long userId = UserContext.getUserId();
        favoriteService.addFavorite(userId, goodsId);
        return Result.success();
    }

    @ApiOperation("批量收藏")
    @PostMapping("/batch/add")
    public Result<Void> batchAddFavorite(@RequestBody List<Long> goodsIds) {
        Long userId = UserContext.getUserId();
        favoriteService.batchAddFavorite(userId, goodsIds);
        return Result.success();
    }

    @ApiOperation("取消收藏")
    @PostMapping("/cancel")
    public Result<Void> cancelFavorite(
            @ApiParam(value = "商品ID", required = true) @RequestParam Long goodsId) {
        Long userId = UserContext.getUserId();
        favoriteService.cancelFavorite(userId, goodsId);
        return Result.success();
    }

    @ApiOperation("批量取消收藏")
    @PostMapping("/batch/cancel")
    public Result<Void> batchCancelFavorite(@RequestBody List<Long> goodsIds) {
        Long userId = UserContext.getUserId();
        favoriteService.batchCancelFavorite(userId, goodsIds);
        return Result.success();
    }

    @ApiOperation("判断是否已收藏")
    @GetMapping("/check")
    public Result<Boolean> isFavorite(
            @ApiParam(value = "商品ID", required = true) @RequestParam Long goodsId) {
        Long userId = UserContext.getUserId();
        boolean result = favoriteService.isFavorite(userId, goodsId);
        return Result.success(result);
    }

    @ApiOperation("获取收藏数量")
    @GetMapping("/count")
    public Result<Integer> getFavoriteCount() {
        Long userId = UserContext.getUserId();
        Integer count = favoriteService.getFavoriteCount(userId);
        return Result.success(count);
    }
}
