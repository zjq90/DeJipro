package com.dj.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.Goods;
import com.dj.mall.entity.UserFavorite;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.GoodsMapper;
import com.dj.mall.mapper.UserFavoriteMapper;
import com.dj.mall.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Page<Map<String, Object>> getFavoriteList(Long userId, Integer pageNum, Integer pageSize) {
        Page<UserFavorite> page = new Page<>(pageNum, pageSize);
        Page<UserFavorite> favoritePage = userFavoriteMapper.selectPage(page,
                new LambdaQueryWrapper<UserFavorite>()
                        .eq(UserFavorite::getUserId, userId)
                        .orderByDesc(UserFavorite::getCreateTime)
        );

        Page<Map<String, Object>> resultPage = new Page<>(pageNum, pageSize);
        resultPage.setTotal(favoritePage.getTotal());
        resultPage.setPages(favoritePage.getPages());
        resultPage.setCurrent(favoritePage.getCurrent());
        resultPage.setSize(favoritePage.getSize());

        List<Map<String, Object>> records = new ArrayList<>();
        for (UserFavorite favorite : favoritePage.getRecords()) {
            Goods goods = goodsMapper.selectById(favorite.getGoodsId());
            if (goods != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("favoriteId", favorite.getId());
                item.put("goodsId", goods.getId());
                item.put("goodsName", goods.getGoodsName());
                item.put("mainImage", goods.getMainImage());
                item.put("sellPrice", goods.getSellPrice());
                item.put("marketPrice", goods.getMarketPrice());
                item.put("integralPrice", goods.getIntegralPrice());
                item.put("isIntegralGoods", goods.getIsIntegralGoods());
                item.put("stock", goods.getStock());
                item.put("status", goods.getStatus());
                item.put("createTime", favorite.getCreateTime());
                records.add(item);
            }
        }
        resultPage.setRecords(records);

        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(Long userId, Long goodsId) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null || goods.getDeleted() == 1) {
            throw new BusinessException("商品不存在");
        }

        UserFavorite exist = userFavoriteMapper.selectOne(
                new LambdaQueryWrapper<UserFavorite>()
                        .eq(UserFavorite::getUserId, userId)
                        .eq(UserFavorite::getGoodsId, goodsId)
        );

        if (exist != null) {
            return;
        }

        UserFavorite favorite = new UserFavorite();
        favorite.setUserId(userId);
        favorite.setGoodsId(goodsId);
        userFavoriteMapper.insert(favorite);

        goodsMapper.update(null,
                new LambdaUpdateWrapper<Goods>()
                        .eq(Goods::getId, goodsId)
                        .setSql("favorite_count = favorite_count + 1")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAddFavorite(Long userId, List<Long> goodsIds) {
        if (goodsIds == null || goodsIds.isEmpty()) {
            return;
        }

        for (Long goodsId : goodsIds) {
            try {
                addFavorite(userId, goodsId);
            } catch (Exception e) {
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelFavorite(Long userId, Long goodsId) {
        UserFavorite favorite = userFavoriteMapper.selectOne(
                new LambdaQueryWrapper<UserFavorite>()
                        .eq(UserFavorite::getUserId, userId)
                        .eq(UserFavorite::getGoodsId, goodsId)
        );

        if (favorite == null) {
            return;
        }

        userFavoriteMapper.deleteById(favorite.getId());

        goodsMapper.update(null,
                new LambdaUpdateWrapper<Goods>()
                        .eq(Goods::getId, goodsId)
                        .setSql("favorite_count = IF(favorite_count > 0, favorite_count - 1, 0)")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCancelFavorite(Long userId, List<Long> goodsIds) {
        if (goodsIds == null || goodsIds.isEmpty()) {
            return;
        }

        for (Long goodsId : goodsIds) {
            try {
                cancelFavorite(userId, goodsId);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public boolean isFavorite(Long userId, Long goodsId) {
        Long count = userFavoriteMapper.selectCount(
                new LambdaQueryWrapper<UserFavorite>()
                        .eq(UserFavorite::getUserId, userId)
                        .eq(UserFavorite::getGoodsId, goodsId)
        );
        return count != null && count > 0;
    }

    @Override
    public Integer getFavoriteCount(Long userId) {
        Long count = userFavoriteMapper.selectCount(
                new LambdaQueryWrapper<UserFavorite>()
                        .eq(UserFavorite::getUserId, userId)
        );
        return count != null ? count.intValue() : 0;
    }
}
