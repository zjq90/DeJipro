package com.dj.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.Article;
import com.dj.mall.entity.ArticleCategory;
import com.dj.mall.entity.Banner;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.ArticleCategoryMapper;
import com.dj.mall.mapper.ArticleMapper;
import com.dj.mall.mapper.BannerMapper;
import com.dj.mall.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private BannerMapper bannerMapper;

    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Banner> getBannerList(Integer bannerType) {
        Date now = new Date();
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(Banner::getStatus, Constants.Status.ENABLE)
                .eq(Banner::getDeleted, 0)
                .le(Banner::getStartTime, now)
                .ge(Banner::getEndTime, now);
        
        if (bannerType != null) {
            wrapper.eq(Banner::getBannerType, bannerType);
        }
        
        wrapper.orderByAsc(Banner::getSort)
                .orderByDesc(Banner::getCreateTime);
        
        return bannerMapper.selectList(wrapper);
    }

    @Override
    public List<ArticleCategory> getCategoryList(Long parentId) {
        if (parentId == null) {
            parentId = 0L;
        }
        
        return articleCategoryMapper.selectList(
                new LambdaQueryWrapper<ArticleCategory>()
                        .eq(ArticleCategory::getParentId, parentId)
                        .eq(ArticleCategory::getStatus, Constants.Status.ENABLE)
                        .eq(ArticleCategory::getDeleted, 0)
                        .orderByAsc(ArticleCategory::getSort)
        );
    }

    @Override
    public Page<Article> getArticlePage(Integer pageNum, Integer pageSize, Long categoryId, String keyword, Integer isHot, Integer isRecommend) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(Article::getStatus, Constants.ArticleStatus.ON_SHELF)
                .eq(Article::getDeleted, 0);
        
        if (categoryId != null && categoryId > 0) {
            wrapper.eq(Article::getCategoryId, categoryId);
        }
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Article::getArticleTitle, keyword)
                    .or().like(Article::getArticleSubtitle, keyword)
                    .or().like(Article::getSummary, keyword));
        }
        
        if (isHot != null && isHot == 1) {
            wrapper.eq(Article::getIsHot, Constants.DefaultFlag.YES);
        }
        
        if (isRecommend != null && isRecommend == 1) {
            wrapper.eq(Article::getIsRecommend, Constants.DefaultFlag.YES);
        }
        
        wrapper.orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getSort)
                .orderByDesc(Article::getPublishTime);
        
        return articleMapper.selectPage(page, wrapper);
    }

    @Override
    public Article getArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null || article.getDeleted() == 1) {
            throw new BusinessException("文章不存在");
        }
        if (!Constants.ArticleStatus.ON_SHELF.equals(article.getStatus())) {
            throw new BusinessException("文章已下架");
        }
        return article;
    }

    @Override
    public Page<Article> getHotArticles(Integer pageNum, Integer pageSize) {
        return getArticlePage(pageNum, pageSize, null, null, 1, null);
    }

    @Override
    public Page<Article> getRecommendArticles(Integer pageNum, Integer pageSize) {
        return getArticlePage(pageNum, pageSize, null, null, null, 1);
    }

    @Override
    public Page<Article> getTopArticles(Integer pageNum, Integer pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        return articleMapper.selectPage(page,
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getIsTop, Constants.DefaultFlag.YES)
                        .eq(Article::getStatus, Constants.ArticleStatus.ON_SHELF)
                        .eq(Article::getDeleted, 0)
                        .orderByDesc(Article::getSort)
                        .orderByDesc(Article::getPublishTime)
        );
    }

    @Override
    public void updateArticleViewCount(Long articleId) {
        articleMapper.update(null,
                new LambdaUpdateWrapper<Article>()
                        .eq(Article::getId, articleId)
                        .setSql("view_count = view_count + 1")
        );
    }
}
