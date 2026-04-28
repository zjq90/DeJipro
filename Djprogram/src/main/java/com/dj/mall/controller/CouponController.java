package com.dj.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Result;
import com.dj.mall.entity.Coupon;
import com.dj.mall.entity.UserCoupon;
import com.dj.mall.service.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(tags = "优惠券接口")
@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @ApiOperation("获取优惠券列表")
    @GetMapping("/list")
    public Result<Page<Coupon>> getCouponList(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "优惠券类型: 1满减券 2折扣券 3无门槛券 4新人券") @RequestParam(required = false) Integer couponType,
            @ApiParam(value = "状态: 0下架 1上架") @RequestParam(required = false) Integer status) {
        Page<Coupon> page = couponService.getCouponPage(pageNum, pageSize, couponType, status);
        return Result.success(page);
    }

    @ApiOperation("获取优惠券详情")
    @GetMapping("/detail/{id}")
    public Result<Coupon> getCouponDetail(@PathVariable Long id) {
        Coupon coupon = couponService.getCouponById(id);
        return Result.success(coupon);
    }

    @ApiOperation("领取优惠券")
    @PostMapping("/receive")
    public Result<UserCoupon> receiveCoupon(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "优惠券ID") @RequestParam Long couponId) {
        UserCoupon userCoupon = couponService.receiveCoupon(userId, couponId);
        return Result.success(userCoupon);
    }

    @ApiOperation("获取我的优惠券列表")
    @GetMapping("/my/list")
    public Result<Page<UserCoupon>> getMyCouponList(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "状态: 0未使用 1已使用 2已过期 3已作废") @RequestParam(required = false) Integer status) {
        Page<UserCoupon> page = couponService.getUserCouponPage(userId, pageNum, pageSize, status);
        return Result.success(page);
    }

    @ApiOperation("获取可用优惠券")
    @GetMapping("/available")
    public Result<List<UserCoupon>> getAvailableCoupons(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "商品ID") @RequestParam(required = false) Long goodsId,
            @ApiParam(value = "订单金额") @RequestParam(required = false) BigDecimal amount) {
        List<UserCoupon> list = couponService.getAvailableCoupons(userId, goodsId, amount);
        return Result.success(list);
    }
}
