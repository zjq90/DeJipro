package com.dj.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.entity.PromotionPoster;
import com.dj.mall.entity.PromotionStat;

import java.util.List;
import java.util.Map;

public interface PromotionService {

    List<PromotionPoster> getPosterList(Integer posterType);

    PromotionPoster getPosterById(Long id);

    Page<PromotionStat> getPromotionStatPage(Long userId, Integer pageNum, Integer pageSize);

    Map<String, Object> getPromotionSummary(Long userId);

    Page<Map<String, Object>> getPromotionRank(Integer pageNum, Integer pageSize);

    Page<Map<String, Object>> getPromotionUserList(Long userId, Integer pageNum, Integer pageSize);

    void recordPromotionView(Long userId, Long viewerId);
}
