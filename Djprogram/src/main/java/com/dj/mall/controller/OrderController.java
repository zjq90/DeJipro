package com.dj.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Result;
import com.dj.mall.entity.OrderInfo;
import com.dj.mall.entity.OrderItem;
import com.dj.mall.entity.PayRecord;
import com.dj.mall.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "订单接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("创建订单")
    @PostMapping("/create")
    public Result<OrderInfo> createOrder(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "订单数据") @RequestBody Map<String, Object> orderData) {
        OrderInfo order = orderService.createOrder(userId, orderData);
        return Result.success(order);
    }

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    public Result<Page<OrderInfo>> getOrderList(
            @ApiParam(value = "用户ID") @RequestParam Long userId,
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "订单状态: 0待付款 1待发货 2待收货 3已完成 4已取消 5已退款") @RequestParam(required = false) Integer orderStatus,
            @ApiParam(value = "订单类型: 1普通订单 2拼团订单 3砍价订单 4积分订单") @RequestParam(required = false) Integer orderType) {
        Page<OrderInfo> page = orderService.getOrderPage(userId, pageNum, pageSize, orderStatus, orderType);
        return Result.success(page);
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> getOrderDetail(@PathVariable Long id) {
        OrderInfo order = orderService.getOrderById(id);
        List<OrderItem> items = orderService.getOrderItems(id);
        
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("order", order);
        result.put("items", items);
        
        return Result.success(result);
    }

    @ApiOperation("取消订单")
    @PostMapping("/cancel")
    public Result<Void> cancelOrder(
            @ApiParam(value = "订单ID") @RequestParam Long orderId,
            @ApiParam(value = "取消原因") @RequestParam(required = false) String reason) {
        orderService.cancelOrder(orderId, reason);
        return Result.success();
    }

    @ApiOperation("确认收货")
    @PostMapping("/confirm")
    public Result<Void> confirmReceive(
            @ApiParam(value = "订单ID") @RequestParam Long orderId) {
        orderService.confirmReceive(orderId);
        return Result.success();
    }

    @ApiOperation("创建支付记录")
    @PostMapping("/pay/create")
    public Result<PayRecord> createPayRecord(
            @ApiParam(value = "订单ID") @RequestParam Long orderId,
            @ApiParam(value = "支付方式: 1支付宝 2微信小程序 3微信App 4微信公众号 5余额支付") @RequestParam Integer payType) {
        PayRecord payRecord = orderService.createPayRecord(orderId, payType);
        return Result.success(payRecord);
    }

    @ApiOperation("查询支付结果")
    @GetMapping("/pay/result")
    public Result<Map<String, Object>> queryPayResult(
            @ApiParam(value = "订单号") @RequestParam String orderNo) {
        Map<String, Object> result = orderService.queryPayResult(orderNo);
        return Result.success(result);
    }

    @ApiOperation("获取订单商品列表")
    @GetMapping("/items/{orderId}")
    public Result<List<OrderItem>> getOrderItems(@PathVariable Long orderId) {
        List<OrderItem> items = orderService.getOrderItems(orderId);
        return Result.success(items);
    }
}
