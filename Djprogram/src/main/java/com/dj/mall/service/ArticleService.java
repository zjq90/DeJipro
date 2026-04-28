package com.dj.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.entity.Article;
import com.dj.mall.entity.ArticleCategory;
import com.dj.mall.entity.Banner;

import java.util.List;

public interface ArticleService {

    List<Banner> getBannerList(Integer bannerType);

    List<ArticleCategory> getCategoryList(Long parentId);

    Page<Article> getArticlePage(Integer pageNum, Integer pageSize, Long categoryId, String keyword, Integer isHot, Integer isRecommend);

    Article getArticleById(Long id);

    Page<Article> getHotArticles(Integer pageNum, Integer pageSize);

    Page<Article> getRecommendArticles(Integer pageNum, Integer pageSize);

    Page<Article> getTopArticles(Integer pageNum, Integer pageSize);

    void updateArticleViewCount(Long articleId);
}
