package com.dj.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.entity.GroupActivity;
import com.dj.mall.entity.GroupGoods;
import com.dj.mall.entity.GroupOrder;

import java.util.List;
import java.util.Map;

public interface GroupService {

    Page<GroupGoods> getGroupGoodsPage(Integer pageNum, Integer pageSize, Integer groupType, Integer isHot, Integer isRecommend);

    GroupGoods getGroupGoodsById(Long id);

    Map<String, Object> getGroupGoodsDetail(Long groupGoodsId);

    GroupActivity createGroupActivity(Long userId, Long groupGoodsId, Long orderId);

    GroupActivity joinGroupActivity(Long userId, Long groupActivityId, Long orderId);

    GroupActivity getGroupActivityById(Long id);

    void cancelGroupActivity(Long groupActivityId);

    Page<GroupActivity> getMyGroupActivities(Long userId, Integer pageNum, Integer pageSize, Integer status);

    List<GroupActivity> getAvailableGroups(Long groupGoodsId);

    Page<GroupGoods> getRecommendGroups(Integer pageNum, Integer pageSize);

    Page<GroupGoods> getHotGroups(Integer pageNum, Integer pageSize);
}
