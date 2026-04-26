package com.dj.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.entity.Goods;
import com.dj.mall.entity.GoodsCategory;
import com.dj.mall.entity.GoodsSku;
import com.dj.mall.entity.GoodsSpecName;

import java.util.List;
import java.util.Map;

public interface GoodsService {

    List<GoodsCategory> getCategoryList(Long parentId);

    Page<Goods> getGoodsPage(Integer pageNum, Integer pageSize, Long categoryId, 
                               String keyword, Integer sortType, Integer isHot, 
                               Integer isRecommend, Integer isNew, Integer isIntegral);

    Map<String, Object> getGoodsDetail(Long goodsId);

    List<GoodsSpecName> getGoodsSpecs(Long goodsId);

    List<GoodsSku> getGoodsSkus(Long goodsId);

    GoodsSku getSkuById(Long skuId);

    Page<Goods> getRecommendGoods(Integer pageNum, Integer pageSize);

    Page<Goods> getHotGoods(Integer pageNum, Integer pageSize);

    Page<Goods> getNewGoods(Integer pageNum, Integer pageSize);

    List<Goods> getGoodsRank(Integer limit);

    void updateGoodsViewCount(Long goodsId);
}
