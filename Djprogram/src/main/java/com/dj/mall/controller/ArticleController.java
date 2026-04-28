package com.dj.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Result;
import com.dj.mall.entity.Article;
import com.dj.mall.entity.ArticleCategory;
import com.dj.mall.entity.Banner;
import com.dj.mall.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "文章接口")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation("获取轮播图列表")
    @GetMapping("/banner/list")
    public Result<List<Banner>> getBannerList(
            @ApiParam(value = "轮播类型: 1首页轮播 2文章轮播 3活动轮播") @RequestParam(required = false) Integer bannerType) {
        List<Banner> list = articleService.getBannerList(bannerType);
        return Result.success(list);
    }

    @ApiOperation("获取文章分类列表")
    @GetMapping("/categories")
    public Result<List<ArticleCategory>> getCategoryList(
            @ApiParam(value = "父分类ID(不传则获取一级分类)") @RequestParam(required = false) Long parentId) {
        List<ArticleCategory> categories = articleService.getCategoryList(parentId);
        return Result.success(categories);
    }

    @ApiOperation("获取文章列表")
    @GetMapping("/list")
    public Result<Page<Article>> getArticleList(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(value = "分类ID") @RequestParam(required = false) Long categoryId,
            @ApiParam(value = "搜索关键词") @RequestParam(required = false) String keyword,
            @ApiParam(value = "是否热门: 0否 1是") @RequestParam(required = false) Integer isHot,
            @ApiParam(value = "是否推荐: 0否 1是") @RequestParam(required = false) Integer isRecommend) {
        Page<Article> page = articleService.getArticlePage(pageNum, pageSize, categoryId, keyword, isHot, isRecommend);
        return Result.success(page);
    }

    @ApiOperation("获取文章详情")
    @GetMapping("/detail/{id}")
    public Result<Article> getArticleDetail(@PathVariable Long id) {
        articleService.updateArticleViewCount(id);
        Article article = articleService.getArticleById(id);
        return Result.success(article);
    }

    @ApiOperation("获取热门文章列表")
    @GetMapping("/hot")
    public Result<Page<Article>> getHotArticles(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Article> page = articleService.getHotArticles(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("获取推荐文章列表")
    @GetMapping("/recommend")
    public Result<Page<Article>> getRecommendArticles(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Article> page = articleService.getRecommendArticles(pageNum, pageSize);
        return Result.success(page);
    }

    @ApiOperation("获取置顶文章列表")
    @GetMapping("/top")
    public Result<Page<Article>> getTopArticles(
            @ApiParam(value = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Article> page = articleService.getTopArticles(pageNum, pageSize);
        return Result.success(page);
    }
}
