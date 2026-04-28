package com.dj.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.entity.BargainActivity;
import com.dj.mall.entity.BargainGoods;
import com.dj.mall.entity.BargainRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BargainService {

    Page<BargainGoods> getBargainGoodsPage(Integer pageNum, Integer pageSize, Integer isHot, Integer isRecommend);

    BargainGoods getBargainGoodsById(Long id);

    Map<String, Object> getBargainGoodsDetail(Long bargainGoodsId);

    BargainActivity createBargainActivity(Long userId, Long bargainGoodsId);

    BargainRecord helpBargain(Long helpUserId, Long bargainActivityId);

    BargainActivity getBargainActivityById(Long id);

    Page<BargainActivity> getMyBargainActivities(Long userId, Integer pageNum, Integer pageSize, Integer status);

    List<BargainRecord> getBargainRecords(Long bargainActivityId);

    Page<BargainGoods> getRecommendBargains(Integer pageNum, Integer pageSize);

    Page<BargainGoods> getHotBargains(Integer pageNum, Integer pageSize);
}
