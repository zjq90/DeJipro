package com.dj.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public interface FavoriteService {

    Page<Map<String, Object>> getFavoriteList(Long userId, Integer pageNum, Integer pageSize);

    void addFavorite(Long userId, Long goodsId);

    void batchAddFavorite(Long userId, List<Long> goodsIds);

    void cancelFavorite(Long userId, Long goodsId);

    void batchCancelFavorite(Long userId, List<Long> goodsIds);

    boolean isFavorite(Long userId, Long goodsId);

    Integer getFavoriteCount(Long userId);
}
