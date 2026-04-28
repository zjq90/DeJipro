package com.dj.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.*;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.*;
import com.dj.mall.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupGoodsMapper groupGoodsMapper;

    @Autowired
    private GroupActivityMapper groupActivityMapper;

    @Autowired
    private GroupOrderMapper groupOrderMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Page<GroupGoods> getGroupGoodsPage(Integer pageNum, Integer pageSize, Integer groupType, Integer isHot, Integer isRecommend) {
        Page<GroupGoods> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<GroupGoods> wrapper = new LambdaQueryWrapper<>();
        
        Date now = new Date();
        wrapper.eq(GroupGoods::getStatus, Constants.Status.ENABLE)
                .eq(GroupGoods::getDeleted, 0)
                .le(GroupGoods::getStartTime, now)
                .ge(GroupGoods::getEndTime, now);
        
        if (groupType != null) {
            wrapper.eq(GroupGoods::getGroupType, groupType);
        }
        
        if (isHot != null && isHot == 1) {
            wrapper.eq(GroupGoods::getIsHot, Constants.DefaultFlag.YES);
        }
        
        if (isRecommend != null && isRecommend == 1) {
            wrapper.eq(GroupGoods::getIsRecommend, Constants.DefaultFlag.YES);
        }
        
        wrapper.orderByAsc(GroupGoods::getSort)
                .orderByDesc(GroupGoods::getCreateTime);
        
        return groupGoodsMapper.selectPage(page, wrapper);
    }

    @Override
    public GroupGoods getGroupGoodsById(Long id) {
        GroupGoods groupGoods = groupGoodsMapper.selectById(id);
        if (groupGoods == null || groupGoods.getDeleted() == 1) {
            throw new BusinessException("拼团商品不存在");
        }
        return groupGoods;
    }

    @Override
    public Map<String, Object> getGroupGoodsDetail(Long groupGoodsId) {
        GroupGoods groupGoods = getGroupGoodsById(groupGoodsId);
        
        if (!Constants.Status.ENABLE.equals(groupGoods.getStatus())) {
            throw new BusinessException("拼团活动已下架");
        }
        
        Date now = new Date();
        if (groupGoods.getStartTime() != null && now.before(groupGoods.getStartTime())) {
            throw new BusinessException("拼团活动未开始");
        }
        if (groupGoods.getEndTime() != null && now.after(groupGoods.getEndTime())) {
            throw new BusinessException("拼团活动已结束");
        }
        
        Goods goods = goodsMapper.selectById(groupGoods.getGoodsId());
        GoodsSku sku = null;
        if (groupGoods.getSkuId() != null) {
            sku = goodsSkuMapper.selectById(groupGoods.getSkuId());
        }
        
        List<GroupActivity> availableGroups = getAvailableGroups(groupGoodsId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("groupGoods", groupGoods);
        result.put("goods", goods);
        result.put("sku", sku);
        result.put("availableGroups", availableGroups);
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GroupActivity createGroupActivity(Long userId, Long groupGoodsId, Long orderId) {
        GroupGoods groupGoods = getGroupGoodsById(groupGoodsId);
        
        if (groupGoods.getTotalStock() > 0 && 
            groupGoods.getUsedStock() >= groupGoods.getTotalStock()) {
            throw new BusinessException("拼团商品库存不足");
        }
        
        if (groupGoods.getLimitBuy() > 0) {
            List<GroupActivity> userActivities = groupActivityMapper.selectList(
                    new LambdaQueryWrapper<GroupActivity>()
                            .eq(GroupActivity::getGroupGoodsId, groupGoodsId)
            );
            
            List<Long> activityIds = new ArrayList<>();
            for (GroupActivity activity : userActivities) {
                activityIds.add(activity.getId());
            }
            
            long userBuyCount = 0L;
            if (!activityIds.isEmpty()) {
                userBuyCount = groupOrderMapper.selectCount(
                        new LambdaQueryWrapper<GroupOrder>()
                                .eq(GroupOrder::getUserId, userId)
                                .in(GroupOrder::getGroupActivityId, activityIds)
                );
            }
            
            if (userBuyCount >= groupGoods.getLimitBuy()) {
                throw new BusinessException("您已达到限购数量");
            }
        }
        
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, groupGoods.getValidHours());
        Date endTime = calendar.getTime();
        
        GroupActivity groupActivity = new GroupActivity();
        groupActivity.setGroupGoodsId(groupGoodsId);
        groupActivity.setLeaderId(userId);
        groupActivity.setLeaderOrderId(orderId);
        groupActivity.setCurrentPeople(1);
        groupActivity.setNeedPeople(groupGoods.getMinPeople());
        groupActivity.setStatus(Constants.GroupActivityStatus.IN_PROGRESS);
        groupActivity.setStartTime(now);
        groupActivity.setEndTime(endTime);
        
        groupActivityMapper.insert(groupActivity);
        
        GroupOrder groupOrder = new GroupOrder();
        groupOrder.setGroupActivityId(groupActivity.getId());
        groupOrder.setUserId(userId);
        groupOrder.setOrderId(orderId);
        groupOrder.setIsLeader(Constants.DefaultFlag.YES);
        groupOrder.setJoinTime(now);
        groupOrder.setStatus(Constants.GroupOrderStatus.PENDING_PAYMENT);
        
        groupOrderMapper.insert(groupOrder);
        
        return groupActivity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GroupActivity joinGroupActivity(Long userId, Long groupActivityId, Long orderId) {
        GroupActivity groupActivity = getGroupActivityById(groupActivityId);
        
        if (!Constants.GroupActivityStatus.IN_PROGRESS.equals(groupActivity.getStatus())) {
            throw new BusinessException("该拼团已结束或已完成");
        }
        
        Date now = new Date();
        if (groupActivity.getEndTime() != null && now.after(groupActivity.getEndTime())) {
            groupActivityMapper.update(null,
                    new LambdaUpdateWrapper<GroupActivity>()
                            .eq(GroupActivity::getId, groupActivityId)
                            .set(GroupActivity::getStatus, Constants.GroupActivityStatus.FAILED)
                            .set(GroupActivity::getFailReason, "拼团时间已过期")
            );
            throw new BusinessException("该拼团已过期");
        }
        
        Long joinCount = groupOrderMapper.selectCount(
                new LambdaQueryWrapper<GroupOrder>()
                        .eq(GroupOrder::getGroupActivityId, groupActivityId)
                        .eq(GroupOrder::getUserId, userId)
        );
        if (joinCount > 0) {
            throw new BusinessException("您已加入该拼团");
        }
        
        if (groupActivity.getCurrentPeople() >= groupActivity.getNeedPeople()) {
            throw new BusinessException("该拼团已满员");
        }
        
        GroupOrder groupOrder = new GroupOrder();
        groupOrder.setGroupActivityId(groupActivityId);
        groupOrder.setUserId(userId);
        groupOrder.setOrderId(orderId);
        groupOrder.setIsLeader(Constants.DefaultFlag.NO);
        groupOrder.setJoinTime(now);
        groupOrder.setStatus(Constants.GroupOrderStatus.PENDING_PAYMENT);
        
        groupOrderMapper.insert(groupOrder);
        
        int newCurrentPeople = groupActivity.getCurrentPeople() + 1;
        groupActivityMapper.update(null,
                new LambdaUpdateWrapper<GroupActivity>()
                        .eq(GroupActivity::getId, groupActivityId)
                        .set(GroupActivity::getCurrentPeople, newCurrentPeople)
        );
        
        if (newCurrentPeople >= groupActivity.getNeedPeople()) {
            groupActivityMapper.update(null,
                    new LambdaUpdateWrapper<GroupActivity>()
                            .eq(GroupActivity::getId, groupActivityId)
                            .set(GroupActivity::getStatus, Constants.GroupActivityStatus.SUCCESS)
                            .set(GroupActivity::getSuccessTime, now)
            );
            groupActivity.setStatus(Constants.GroupActivityStatus.SUCCESS);
            groupActivity.setSuccessTime(now);
        }
        
        groupActivity.setCurrentPeople(newCurrentPeople);
        return groupActivity;
    }

    @Override
    public GroupActivity getGroupActivityById(Long id) {
        GroupActivity groupActivity = groupActivityMapper.selectById(id);
        if (groupActivity == null) {
            throw new BusinessException("拼团活动不存在");
        }
        return groupActivity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelGroupActivity(Long groupActivityId) {
        GroupActivity groupActivity = getGroupActivityById(groupActivityId);
        
        if (!Constants.GroupActivityStatus.IN_PROGRESS.equals(groupActivity.getStatus())) {
            throw new BusinessException("该拼团无法取消");
        }
        
        groupActivityMapper.update(null,
                new LambdaUpdateWrapper<GroupActivity>()
                        .eq(GroupActivity::getId, groupActivityId)
                        .set(GroupActivity::getStatus, Constants.GroupActivityStatus.FAILED)
                        .set(GroupActivity::getFailReason, "用户主动取消")
        );
        
        groupOrderMapper.update(null,
                new LambdaUpdateWrapper<GroupOrder>()
                        .eq(GroupOrder::getGroupActivityId, groupActivityId)
                        .set(GroupOrder::getStatus, Constants.GroupOrderStatus.CANCELLED)
        );
    }

    @Override
    public Page<GroupActivity> getMyGroupActivities(Long userId, Integer pageNum, Integer pageSize, Integer status) {
        Page<GroupActivity> page = new Page<>(pageNum, pageSize);
        
        List<Long> activityIds = new ArrayList<>();
        List<GroupOrder> groupOrders = groupOrderMapper.selectList(
                new LambdaQueryWrapper<GroupOrder>()
                        .eq(GroupOrder::getUserId, userId)
        );
        
        for (GroupOrder order : groupOrders) {
            if (!activityIds.contains(order.getGroupActivityId())) {
                activityIds.add(order.getGroupActivityId());
            }
        }
        
        if (activityIds.isEmpty()) {
            return page;
        }
        
        LambdaQueryWrapper<GroupActivity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(GroupActivity::getId, activityIds);
        
        if (status != null) {
            wrapper.eq(GroupActivity::getStatus, status);
        }
        
        wrapper.orderByDesc(GroupActivity::getCreateTime);
        
        return groupActivityMapper.selectPage(page, wrapper);
    }

    @Override
    public List<GroupActivity> getAvailableGroups(Long groupGoodsId) {
        Date now = new Date();
        List<GroupActivity> list = groupActivityMapper.selectList(
                new LambdaQueryWrapper<GroupActivity>()
                        .eq(GroupActivity::getGroupGoodsId, groupGoodsId)
                        .eq(GroupActivity::getStatus, Constants.GroupActivityStatus.IN_PROGRESS)
                        .ge(GroupActivity::getEndTime, now)
                        .orderByDesc(GroupActivity::getCreateTime)
        );
        
        List<GroupActivity> result = new ArrayList<>();
        for (GroupActivity activity : list) {
            if (activity.getNeedPeople() > activity.getCurrentPeople()) {
                result.add(activity);
            }
            if (result.size() >= 10) {
                break;
            }
        }
        
        return result;
    }

    @Override
    public Page<GroupGoods> getRecommendGroups(Integer pageNum, Integer pageSize) {
        return getGroupGoodsPage(pageNum, pageSize, null, null, 1);
    }

    @Override
    public Page<GroupGoods> getHotGroups(Integer pageNum, Integer pageSize) {
        return getGroupGoodsPage(pageNum, pageSize, null, 1, null);
    }
}
