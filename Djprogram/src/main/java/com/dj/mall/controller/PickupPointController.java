package com.dj.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Result;
import com.dj.mall.entity.PickupPoint;
import com.dj.mall.service.PickupPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(tags = "提货点接口")
@RestController
@RequestMapping("/pickup")
public class PickupPointController {

    @Autowired
    private PickupPointService pickupPointService;

    @ApiOperation("获取附近提货点")
    @GetMapping("/nearby")
    public Result<List<PickupPoint>> getNearbyPoints(
            @ApiParam(value = "经度") @RequestParam(required = false) BigDecimal longitude,
            @ApiParam(value = "纬度") @RequestParam(required = false) BigDecimal latitude,
            @ApiParam(value = "搜索半径(公里)") @RequestParam(required = false) BigDecimal radius,
            @ApiParam(value = "返回数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<PickupPoint> list = pickupPointService.getNearbyPoints(longitude, latitude, radius, limit);
        return Result.success(list);
    }

    @ApiOperation("获取提货点列表")
    @GetMapping("/list")
    public Result<Page<PickupPoint>> getPickupPointList(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "省份") @RequestParam(required = false) String province,
            @ApiParam(value = "城市") @RequestParam(required = false) String city,
            @ApiParam(value = "区县") @RequestParam(required = false) String district) {
        Page<PickupPoint> page = pickupPointService.getPickupPointPage(pageNum, pageSize, province, city, district);
        return Result.success(page);
    }

    @ApiOperation("获取提货点详情")
    @GetMapping("/detail/{id}")
    public Result<PickupPoint> getPickupPointDetail(@PathVariable Long id) {
        PickupPoint point = pickupPointService.getPickupPointById(id);
        return Result.success(point);
    }

    @ApiOperation("获取城市提货点列表")
    @GetMapping("/city/{city}")
    public Result<List<PickupPoint>> getPointsByCity(@PathVariable String city) {
        List<PickupPoint> list = pickupPointService.getPointsByCity(city);
        return Result.success(list);
    }
}
