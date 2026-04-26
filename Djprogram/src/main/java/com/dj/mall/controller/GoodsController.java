package com.dj.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Result;
import com.dj.mall.entity.Goods;
import com.dj.mall.entity.GoodsCategory;
import com.dj.mall.entity.GoodsSku;
import com.dj.mall.entity.GoodsSpecName;
import com.dj.mall.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "商品接口")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @ApiOperation("获取商品分类列表")
    @GetMapping("/categories")
    public Result<List<GoodsCategory>> getCategoryList(
            @ApiParam(value = "父分类ID(不传则获取一级分类)") @RequestParam(required = false) Long parentId) {
        List<GoodsCategory> categories = goodsService.getCategoryList(parentId);
        return Result.success(categories);
    }

    @ApiOperation("获取商品列表")
    @GetMapping("/list")
    public Result<Page<Goods>> getGoodsList(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "分类ID") @RequestParam(required = false) Long categoryId,
            @ApiParam(value = "搜索关键词") @RequestParam(required = false) String keyword,
            @ApiParam(value = "排序类型: 1价格升序 2价格降序 3销量降序 4最新") @RequestParam(required = false) Integer sortType,
            @ApiParam(value = "是否热门: 0否 1是") @RequestParam(required = false) Integer isHot,
            @ApiParam(value = "是否推荐: 0否 1是") @RequestParam(required = false) Integer isRecommend,
            @ApiParam(value = "是否新品: 0否 1是") @RequestParam(required = false) Integer isNew,
            @ApiParam(value = "是否积分商品: 0否 1是") @RequestParam(required = false) Integer isIntegral) {
        Page<Goods> page = goodsService.getGoodsPage(pageNum, pageSize, categoryId,
                keyword, sortType, isHot, isRecommend, isNew, isIntegral);
        return Result.success(page);
    }

    @ApiOperation("获取商品详情")
    @GetMapping("/detail/{goodsId}")
    public Result<Map<String, Object>> getGoodsDetail(@PathVariable Long goodsId) {
        goodsService.updateGoodsViewCount(goodsId);
        Map<String, Object> detail = goodsService.getGoodsDetail(goodsId);
        return Result.success(detail);
    }

    @ApiOperation("获取商品规格")
    @GetMapping("/specs/{goodsId}")
    public Result<List<GoodsSpecName>> getGoodsSpecs(@PathVariable Long goodsId) {
        List<GoodsSpecName> specs = goodsService.getGoodsSpecs(goodsId);
        return Result.success(specs);
    }

    @ApiOperation("获取商品SKU列表")
    @GetMapping("/skus/{goodsId}")
    public Result<List<GoodsSku>> getGoodsSkus(@PathVariable Long goodsId) {
        List<GoodsSku> skus = goodsService.getGoodsSkus(goodsId);
        return Result.success(skus);
    }

    @ApiOperation("获取推荐商品")
    @GetMapping("/recommend")
    public Result<Page<Goods>> getRecommendGoods(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Goods> page = goodsService.getRecommendGoods(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("获取热门商品")
    @GetMapping("/hot")
    public Result<Page<Goods>> getHotGoods(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Goods> page = goodsService.getHotGoods(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("获取新品列表")
    @GetMapping("/new")
    public Result<Page<Goods>> getNewGoods(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Goods> page = goodsService.getNewGoods(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("获取商品排行榜")
    @GetMapping("/rank")
    public Result<List<Goods>> getGoodsRank(
            @ApiParam(value = "数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<Goods> list = goodsService.getGoodsRank(limit);
        return Result.success(list);
    }

    @ApiOperation("获取积分商品列表")
    @GetMapping("/integral")
    public Result<Page<Goods>> getIntegralGoods(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Goods> page = goodsService.getGoodsPage(pageNum, pageSize, null,
                null, null, null, null, null, 1);
        return Result.success(page);
    }
}
