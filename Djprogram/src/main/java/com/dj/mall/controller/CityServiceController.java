package com.dj.mall.controller;

import com.dj.mall.common.Result;
import com.dj.mall.entity.CityService;
import com.dj.mall.service.CityServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "城市服务接口")
@RestController
@RequestMapping("/city-service")
public class CityServiceController {

    @Autowired
    private CityServiceService cityServiceService;

    @ApiOperation("获取服务列表(树形结构)")
    @GetMapping("/tree")
    public Result<List<CityService>> getServiceTree() {
        List<CityService> list = cityServiceService.getServiceTree();
        return Result.success(list);
    }

    @ApiOperation("获取服务列表")
    @GetMapping("/list")
    public Result<List<CityService>> getServiceList(
            @ApiParam(value = "父服务ID(不传则获取一级分类)") @RequestParam(required = false) Long parentId) {
        List<CityService> list = cityServiceService.getServiceList(parentId);
        return Result.success(list);
    }

    @ApiOperation("获取服务详情")
    @GetMapping("/detail/{id}")
    public Result<CityService> getServiceDetail(@PathVariable Long id) {
        CityService service = cityServiceService.getServiceById(id);
        return Result.success(service);
    }
}
