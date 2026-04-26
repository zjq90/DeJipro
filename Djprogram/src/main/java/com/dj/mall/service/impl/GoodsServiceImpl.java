package com.dj.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.*;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.*;
import com.dj.mall.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Autowired
    private GoodsSpecNameMapper goodsSpecNameMapper;

    @Autowired
    private GoodsSpecValueMapper goodsSpecValueMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Override
    public List<GoodsCategory> getCategoryList(Long parentId) {
        if (parentId == null) {
            parentId = 0L;
        }
        return goodsCategoryMapper.selectList(
                new LambdaQueryWrapper<GoodsCategory>()
                        .eq(GoodsCategory::getParentId, parentId)
                        .eq(GoodsCategory::getStatus, Constants.Status.ENABLE)
                        .eq(GoodsCategory::getDeleted, 0)
                        .orderByAsc(GoodsCategory::getSort)
        );
    }

    @Override
    public Page<Goods> getGoodsPage(Integer pageNum, Integer pageSize, Long categoryId,
                                      String keyword, Integer sortType, Integer isHot,
                                      Integer isRecommend, Integer isNew, Integer isIntegral) {
        Page<Goods> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(Goods::getStatus, Constants.GoodsStatus.ON_SHELF)
                .eq(Goods::getDeleted, 0);

        if (categoryId != null && categoryId > 0) {
            wrapper.eq(Goods::getCategoryId, categoryId);
        }

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Goods::getGoodsName, keyword)
                    .or().like(Goods::getGoodsSubtitle, keyword));
        }

        if (isHot != null && isHot == 1) {
            wrapper.eq(Goods::getIsHot, Constants.DefaultFlag.YES);
        }

        if (isRecommend != null && isRecommend == 1) {
            wrapper.eq(Goods::getIsRecommend, Constants.DefaultFlag.YES);
        }

        if (isNew != null && isNew == 1) {
            wrapper.eq(Goods::getIsNew, Constants.DefaultFlag.YES);
        }

        if (isIntegral != null && isIntegral == 1) {
            wrapper.eq(Goods::getIsIntegralGoods, Constants.DefaultFlag.YES);
        }

        if (sortType != null) {
            switch (sortType) {
                case 1:
                    wrapper.orderByAsc(Goods::getSellPrice);
                    break;
                case 2:
                    wrapper.orderByDesc(Goods::getSellPrice);
                    break;
                case 3:
                    wrapper.orderByDesc(Goods::getSellCount);
                    break;
                case 4:
                    wrapper.orderByDesc(Goods::getCreateTime);
                    break;
                default:
                    wrapper.orderByDesc(Goods::getSort)
                            .orderByDesc(Goods::getCreateTime);
            }
        } else {
            wrapper.orderByDesc(Goods::getSort)
                    .orderByDesc(Goods::getCreateTime);
        }

        return goodsMapper.selectPage(page, wrapper);
    }

    @Override
    public Map<String, Object> getGoodsDetail(Long goodsId) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null || goods.getDeleted() == 1) {
            throw new BusinessException("商品不存在");
        }
        if (goods.getStatus() == Constants.GoodsStatus.OFF_SHELF) {
            throw new BusinessException("商品已下架");
        }

        List<GoodsSpecName> specs = getGoodsSpecs(goodsId);
        List<GoodsSku> skus = getGoodsSkus(goodsId);

        Map<String, Object> result = new HashMap<>();
        result.put("goods", goods);
        result.put("specs", specs);
        result.put("skus", skus);

        return result;
    }

    @Override
    public List<GoodsSpecName> getGoodsSpecs(Long goodsId) {
        List<GoodsSpecName> specNames = goodsSpecNameMapper.selectList(
                new LambdaQueryWrapper<GoodsSpecName>()
                        .eq(GoodsSpecName::getGoodsId, goodsId)
                        .orderByAsc(GoodsSpecName::getSort)
        );

        for (GoodsSpecName specName : specNames) {
            List<GoodsSpecValue> specValues = goodsSpecValueMapper.selectList(
                    new LambdaQueryWrapper<GoodsSpecValue>()
                            .eq(GoodsSpecValue::getSpecNameId, specName.getId())
                            .eq(GoodsSpecValue::getGoodsId, goodsId)
                            .orderByAsc(GoodsSpecValue::getSort)
            );
        }

        return specNames;
    }

    @Override
    public List<GoodsSku> getGoodsSkus(Long goodsId) {
        return goodsSkuMapper.selectList(
                new LambdaQueryWrapper<GoodsSku>()
                        .eq(GoodsSku::getGoodsId, goodsId)
                        .eq(GoodsSku::getStatus, Constants.Status.ENABLE)
        );
    }

    @Override
    public GoodsSku getSkuById(Long skuId) {
        return goodsSkuMapper.selectById(skuId);
    }

    @Override
    public Page<Goods> getRecommendGoods(Integer pageNum, Integer pageSize) {
        return getGoodsPage(pageNum, pageSize, null, null, null, null, 1, null, null);
    }

    @Override
    public Page<Goods> getHotGoods(Integer pageNum, Integer pageSize) {
        return getGoodsPage(pageNum, pageSize, null, null, null, 1, null, null, null);
    }

    @Override
    public Page<Goods> getNewGoods(Integer pageNum, Integer pageSize) {
        return getGoodsPage(pageNum, pageSize, null, null, 4, null, null, 1, null);
    }

    @Override
    public List<Goods> getGoodsRank(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        return goodsMapper.selectList(
                new LambdaQueryWrapper<Goods>()
                        .eq(Goods::getStatus, Constants.GoodsStatus.ON_SHELF)
                        .eq(Goods::getDeleted, 0)
                        .orderByDesc(Goods::getSellCount)
                        .last("LIMIT " + limit)
        );
    }

    @Override
    public void updateGoodsViewCount(Long goodsId) {
        goodsMapper.update(null,
                new LambdaUpdateWrapper<Goods>()
                        .eq(Goods::getId, goodsId)
                        .setSql("view_count = view_count + 1")
        );
    }
}
