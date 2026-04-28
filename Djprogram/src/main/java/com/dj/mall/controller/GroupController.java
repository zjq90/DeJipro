package com.dj.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Result;
import com.dj.mall.entity.GroupActivity;
import com.dj.mall.entity.GroupGoods;
import com.dj.mall.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "拼团接口")
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @ApiOperation("获取拼团商品列表")
    @GetMapping("/list")
    public Result<Page<GroupGoods>> getGroupGoodsList(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "拼团类型: 1普通拼团 2老带新拼团 3团长免单") @RequestParam(required = false) Integer groupType,
            @ApiParam(value = "是否热门: 0否 1是") @RequestParam(required = false) Integer isHot,
            @ApiParam(value = "是否推荐: 0否 1是") @RequestParam(required = false) Integer isRecommend) {
        Page<GroupGoods> page = groupService.getGroupGoodsPage(pageNum, pageSize, groupType, isHot, isRecommend);
        return Result.success(page);
    }

    @ApiOperation("获取拼团商品详情")
    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> getGroupGoodsDetail(@PathVariable Long id) {
        Map<String, Object> detail = groupService.getGroupGoodsDetail(id);
        return Result.success(detail);
    }

    @ApiOperation("获取拼团首页")
    @GetMapping("/home")
    public Result<Map<String, Object>> getGroupHome(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<GroupGoods> recommendPage = groupService.getRecommendGroups(pageNum, pageSize);
        Page<GroupGoods> hotPage = groupService.getHotGroups(pageNum, pageSize);
        
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("recommendList", recommendPage.getRecords());
        result.put("hotList", hotPage.getRecords());
        
        return Result.success(result);
    }

    @ApiOperation("发起拼团")
    @PostMapping("/create")
    public Result<GroupActivity> createGroup(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "拼团商品ID") @RequestParam Long groupGoodsId,
            @ApiParam(value = "订单ID") @RequestParam Long orderId) {
        GroupActivity activity = groupService.createGroupActivity(userId, groupGoodsId, orderId);
        return Result.success(activity);
    }

    @ApiOperation("加入拼团")
    @PostMapping("/join")
    public Result<GroupActivity> joinGroup(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "拼团活动ID") @RequestParam Long groupActivityId,
            @ApiParam(value = "订单ID") @RequestParam Long orderId) {
        GroupActivity activity = groupService.joinGroupActivity(userId, groupActivityId, orderId);
        return Result.success(activity);
    }

    @ApiOperation("取消拼团")
    @PostMapping("/cancel")
    public Result<Void> cancelGroup(
            @ApiParam(value = "拼团活动ID") @RequestParam Long groupActivityId) {
        groupService.cancelGroupActivity(groupActivityId);
        return Result.success();
    }

    @ApiOperation("获取拼团活动详情")
    @GetMapping("/activity/{id}")
    public Result<GroupActivity> getGroupActivityDetail(@PathVariable Long id) {
        GroupActivity activity = groupService.getGroupActivityById(id);
        return Result.success(activity);
    }

    @ApiOperation("获取我的拼团列表")
    @GetMapping("/my/list")
    public Result<Page<GroupActivity>> getMyGroupList(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "状态: 0进行中 1已成团 2已失败") @RequestParam(required = false) Integer status) {
        Page<GroupActivity> page = groupService.getMyGroupActivities(userId, pageNum, pageSize, status);
        return Result.success(page);
    }

    @ApiOperation("获取可加入的拼团列表")
    @GetMapping("/available/{groupGoodsId}")
    public Result<List<GroupActivity>> getAvailableGroups(@PathVariable Long groupGoodsId) {
        List<GroupActivity> list = groupService.getAvailableGroups(groupGoodsId);
        return Result.success(list);
    }

    @ApiOperation("获取更多拼团")
    @GetMapping("/more")
    public Result<Page<GroupGoods>> getMoreGroups(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "拼团类型: 1普通拼团 2老带新拼团 3团长免单") @RequestParam(required = false) Integer groupType) {
        Page<GroupGoods> page = groupService.getGroupGoodsPage(pageNum, pageSize, groupType, null, null);
        return Result.success(page);
    }

    @ApiOperation("获取推荐拼团")
    @GetMapping("/recommend")
    public Result<Page<GroupGoods>> getRecommendGroups(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<GroupGoods> page = groupService.getRecommendGroups(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("获取热门拼团")
    @GetMapping("/hot")
    public Result<Page<GroupGoods>> getHotGroups(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<GroupGoods> page = groupService.getHotGroups(pageNum, pageSize);
        return Result.success(page);
    }
}
