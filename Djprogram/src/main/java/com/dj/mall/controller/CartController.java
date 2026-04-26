package com.dj.mall.controller;

import com.dj.mall.common.Result;
import com.dj.mall.context.UserContext;
import com.dj.mall.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "购物车接口")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @ApiOperation("获取购物车列表")
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getCartList() {
        Long userId = UserContext.getUserId();
        List<Map<String, Object>> list = cartService.getCartList(userId);
        return Result.success(list);
    }

    @ApiOperation("获取购物车商品数量")
    @GetMapping("/count")
    public Result<Integer> getCartCount() {
        Long userId = UserContext.getUserId();
        Integer count = cartService.getCartCount(userId);
        return Result.success(count);
    }

    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public Result<Void> addCart(
            @ApiParam(value = "商品ID", required = true) @RequestParam Long goodsId,
            @ApiParam(value = "SKU ID", required = true) @RequestParam Long skuId,
            @ApiParam(value = "数量", required = true) @RequestParam Integer quantity) {
        Long userId = UserContext.getUserId();
        cartService.addCart(userId, goodsId, skuId, quantity);
        return Result.success();
    }

    @ApiOperation("更新购物车商品数量")
    @PostMapping("/update")
    public Result<Void> updateCart(
            @ApiParam(value = "购物车ID", required = true) @RequestParam Long cartId,
            @ApiParam(value = "数量", required = true) @RequestParam Integer quantity) {
        Long userId = UserContext.getUserId();
        cartService.updateCart(userId, cartId, quantity);
        return Result.success();
    }

    @ApiOperation("更新选中状态")
    @PostMapping("/selected")
    public Result<Void> updateSelected(
            @ApiParam(value = "购物车ID列表", required = true) @RequestBody List<Long> cartIds,
            @ApiParam(value = "选中状态: 0未选 1已选", required = true) @RequestParam Integer selected) {
        Long userId = UserContext.getUserId();
        cartService.updateSelected(userId, cartIds, selected);
        return Result.success();
    }

    @ApiOperation("全选/取消全选")
    @PostMapping("/selectAll")
    public Result<Void> selectAll(
            @ApiParam(value = "选中状态: 0取消全选 1全选", required = true) @RequestParam Integer selected) {
        Long userId = UserContext.getUserId();
        cartService.selectAll(userId, selected);
        return Result.success();
    }

    @ApiOperation("删除购物车商品")
    @PostMapping("/delete")
    public Result<Void> deleteCart(@RequestBody List<Long> cartIds) {
        Long userId = UserContext.getUserId();
        cartService.deleteCart(userId, cartIds);
        return Result.success();
    }

    @ApiOperation("清空购物车")
    @PostMapping("/clear")
    public Result<Void> clearCart() {
        Long userId = UserContext.getUserId();
        cartService.clearCart(userId);
        return Result.success();
    }

    @ApiOperation("获取已选中的购物车列表(用于结算)")
    @GetMapping("/selected")
    public Result<List<Map<String, Object>>> getSelectedCartList() {
        Long userId = UserContext.getUserId();
        List<Map<String, Object>> list = cartService.getSelectedCartList(userId);
        return Result.success(list);
    }
}
