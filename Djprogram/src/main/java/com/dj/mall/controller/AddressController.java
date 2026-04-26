package com.dj.mall.controller;

import com.dj.mall.common.Result;
import com.dj.mall.context.UserContext;
import com.dj.mall.entity.UserAddress;
import com.dj.mall.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "地址管理接口")
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation("获取地址列表")
    @GetMapping("/list")
    public Result<List<UserAddress>> getAddressList() {
        Long userId = UserContext.getUserId();
        List<UserAddress> list = addressService.getAddressList(userId);
        return Result.success(list);
    }

    @ApiOperation("获取地址详情")
    @GetMapping("/detail/{addressId}")
    public Result<UserAddress> getAddressDetail(@PathVariable Long addressId) {
        Long userId = UserContext.getUserId();
        UserAddress address = addressService.getAddressDetail(userId, addressId);
        return Result.success(address);
    }

    @ApiOperation("获取默认地址")
    @GetMapping("/default")
    public Result<UserAddress> getDefaultAddress() {
        Long userId = UserContext.getUserId();
        UserAddress address = addressService.getDefaultAddress(userId);
        return Result.success(address);
    }

    @ApiOperation("添加地址")
    @PostMapping("/add")
    public Result<Void> addAddress(@RequestBody UserAddress address) {
        Long userId = UserContext.getUserId();
        addressService.addAddress(userId, address);
        return Result.success();
    }

    @ApiOperation("更新地址")
    @PostMapping("/update")
    public Result<Void> updateAddress(@RequestBody UserAddress address) {
        Long userId = UserContext.getUserId();
        addressService.updateAddress(userId, address);
        return Result.success();
    }

    @ApiOperation("删除地址")
    @PostMapping("/delete")
    public Result<Void> deleteAddress(
            @ApiParam(value = "地址ID", required = true) @RequestParam Long addressId) {
        Long userId = UserContext.getUserId();
        addressService.deleteAddress(userId, addressId);
        return Result.success();
    }

    @ApiOperation("设置默认地址")
    @PostMapping("/default")
    public Result<Void> setDefaultAddress(
            @ApiParam(value = "地址ID", required = true) @RequestParam Long addressId) {
        Long userId = UserContext.getUserId();
        addressService.setDefaultAddress(userId, addressId);
        return Result.success();
    }
}
