package com.dj.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Result;
import com.dj.mall.entity.BargainActivity;
import com.dj.mall.entity.BargainGoods;
import com.dj.mall.entity.BargainRecord;
import com.dj.mall.service.BargainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "砍价接口")
@RestController
@RequestMapping("/bargain")
public class BargainController {

    @Autowired
    private BargainService bargainService;

    @ApiOperation("获取砍价商品列表")
    @GetMapping("/list")
    public Result<Page<BargainGoods>> getBargainGoodsList(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "是否热门: 0否 1是") @RequestParam(required = false) Integer isHot,
            @ApiParam(value = "是否推荐: 0否 1是") @RequestParam(required = false) Integer isRecommend) {
        Page<BargainGoods> page = bargainService.getBargainGoodsPage(pageNum, pageSize, isHot, isRecommend);
        return Result.success(page);
    }

    @ApiOperation("获取砍价商品详情")
    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> getBargainGoodsDetail(@PathVariable Long id) {
        Map<String, Object> detail = bargainService.getBargainGoodsDetail(id);
        return Result.success(detail);
    }

    @ApiOperation("发起砍价")
    @PostMapping("/create")
    public Result<BargainActivity> createBargain(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "砍价商品ID") @RequestParam Long bargainGoodsId) {
        BargainActivity activity = bargainService.createBargainActivity(userId, bargainGoodsId);
        return Result.success(activity);
    }

    @ApiOperation("帮砍价")
    @PostMapping("/help")
    public Result<BargainRecord> helpBargain(
            @ApiParam(value = "帮砍用户ID") @RequestParam Long helpUserId,
            @ApiParam(value = "砍价活动ID") @RequestParam Long bargainActivityId) {
        BargainRecord record = bargainService.helpBargain(helpUserId, bargainActivityId);
        return Result.success(record);
    }

    @ApiOperation("获取砍价活动详情")
    @GetMapping("/activity/{id}")
    public Result<Map<String, Object>> getBargainActivityDetail(@PathVariable Long id) {
        BargainActivity activity = bargainService.getBargainActivityById(id);
        List<BargainRecord> records = bargainService.getBargainRecords(id);
        
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("activity", activity);
        result.put("records", records);
        
        return Result.success(result);
    }

    @ApiOperation("获取我的砍价列表")
    @GetMapping("/my/list")
    public Result<Page<BargainActivity>> getMyBargainList(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "状态: 0进行中 1已完成 2已购买 3已过期 4已取消") @RequestParam(required = false) Integer status) {
        Page<BargainActivity> page = bargainService.getMyBargainActivities(userId, pageNum, pageSize, status);
        return Result.success(page);
    }

    @ApiOperation("获取砍价记录")
    @GetMapping("/records/{activityId}")
    public Result<List<BargainRecord>> getBargainRecords(@PathVariable Long activityId) {
        List<BargainRecord> list = bargainService.getBargainRecords(activityId);
        return Result.success(list);
    }

    @ApiOperation("获取推荐砍价商品")
    @GetMapping("/recommend")
    public Result<Page<BargainGoods>> getRecommendBargains(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<BargainGoods> page = bargainService.getRecommendBargains(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("获取热门砍价商品")
    @GetMapping("/hot")
    public Result<Page<BargainGoods>> getHotBargains(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<BargainGoods> page = bargainService.getHotBargains(pageNum, pageSize);
        return Result.success(page);
    }
}
