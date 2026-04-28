package com.dj.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.*;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.*;
import com.dj.mall.service.BargainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BargainServiceImpl implements BargainService {

    @Autowired
    private BargainGoodsMapper bargainGoodsMapper;

    @Autowired
    private BargainActivityMapper bargainActivityMapper;

    @Autowired
    private BargainRecordMapper bargainRecordMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Page<BargainGoods> getBargainGoodsPage(Integer pageNum, Integer pageSize, Integer isHot, Integer isRecommend) {
        Page<BargainGoods> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BargainGoods> wrapper = new LambdaQueryWrapper<>();
        
        Date now = new Date();
        wrapper.eq(BargainGoods::getStatus, Constants.Status.ENABLE)
                .eq(BargainGoods::getDeleted, 0)
                .le(BargainGoods::getStartTime, now)
                .ge(BargainGoods::getEndTime, now);
        
        if (isHot != null && isHot == 1) {
            wrapper.eq(BargainGoods::getIsHot, Constants.DefaultFlag.YES);
        }
        
        if (isRecommend != null && isRecommend == 1) {
            wrapper.eq(BargainGoods::getIsRecommend, Constants.DefaultFlag.YES);
        }
        
        wrapper.orderByAsc(BargainGoods::getSort)
                .orderByDesc(BargainGoods::getCreateTime);
        
        return bargainGoodsMapper.selectPage(page, wrapper);
    }

    @Override
    public BargainGoods getBargainGoodsById(Long id) {
        BargainGoods bargainGoods = bargainGoodsMapper.selectById(id);
        if (bargainGoods == null || bargainGoods.getDeleted() == 1) {
            throw new BusinessException("砍价商品不存在");
        }
        return bargainGoods;
    }

    @Override
    public Map<String, Object> getBargainGoodsDetail(Long bargainGoodsId) {
        BargainGoods bargainGoods = getBargainGoodsById(bargainGoodsId);
        
        if (!Constants.Status.ENABLE.equals(bargainGoods.getStatus())) {
            throw new BusinessException("砍价活动已下架");
        }
        
        Date now = new Date();
        if (bargainGoods.getStartTime() != null && now.before(bargainGoods.getStartTime())) {
            throw new BusinessException("砍价活动未开始");
        }
        if (bargainGoods.getEndTime() != null && now.after(bargainGoods.getEndTime())) {
            throw new BusinessException("砍价活动已结束");
        }
        
        Goods goods = goodsMapper.selectById(bargainGoods.getGoodsId());
        GoodsSku sku = null;
        if (bargainGoods.getSkuId() != null) {
            sku = goodsSkuMapper.selectById(bargainGoods.getSkuId());
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("bargainGoods", bargainGoods);
        result.put("goods", goods);
        result.put("sku", sku);
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BargainActivity createBargainActivity(Long userId, Long bargainGoodsId) {
        BargainGoods bargainGoods = getBargainGoodsById(bargainGoodsId);
        
        if (bargainGoods.getTotalStock() > 0 && 
            bargainGoods.getUsedStock() >= bargainGoods.getTotalStock()) {
            throw new BusinessException("砍价商品库存不足");
        }
        
        if (bargainGoods.getLimitBuy() > 0) {
            Long userBuyCount = bargainActivityMapper.selectCount(
                    new LambdaQueryWrapper<BargainActivity>()
                            .eq(BargainActivity::getUserId, userId)
                            .eq(BargainActivity::getBargainGoodsId, bargainGoodsId)
                            .in(BargainActivity::getStatus, 
                                Arrays.asList(Constants.BargainActivityStatus.IN_PROGRESS, 
                                            Constants.BargainActivityStatus.COMPLETED,
                                            Constants.BargainActivityStatus.PURCHASED))
            );
            if (userBuyCount >= bargainGoods.getLimitBuy()) {
                throw new BusinessException("您已达到限购数量");
            }
        }
        
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, bargainGoods.getValidHours());
        Date endTime = calendar.getTime();
        
        BargainActivity bargainActivity = new BargainActivity();
        bargainActivity.setBargainGoodsId(bargainGoodsId);
        bargainActivity.setUserId(userId);
        bargainActivity.setCurrentPrice(bargainGoods.getOriginalPrice());
        bargainActivity.setBargainAmount(BigDecimal.ZERO);
        bargainActivity.setBargainCount(0);
        bargainActivity.setStatus(Constants.BargainActivityStatus.IN_PROGRESS);
        bargainActivity.setStartTime(now);
        bargainActivity.setEndTime(endTime);
        
        bargainActivityMapper.insert(bargainActivity);
        
        return bargainActivity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BargainRecord helpBargain(Long helpUserId, Long bargainActivityId) {
        BargainActivity bargainActivity = getBargainActivityById(bargainActivityId);
        
        if (!Constants.BargainActivityStatus.IN_PROGRESS.equals(bargainActivity.getStatus())) {
            throw new BusinessException("该砍价活动已结束或已完成");
        }
        
        Date now = new Date();
        if (bargainActivity.getEndTime() != null && now.after(bargainActivity.getEndTime())) {
            bargainActivityMapper.update(null,
                    new LambdaUpdateWrapper<BargainActivity>()
                            .eq(BargainActivity::getId, bargainActivityId)
                            .set(BargainActivity::getStatus, Constants.BargainActivityStatus.EXPIRED)
            );
            throw new BusinessException("该砍价已过期");
        }
        
        if (helpUserId != null && helpUserId.equals(bargainActivity.getUserId())) {
            throw new BusinessException("不能帮自己砍价");
        }
        
        BargainGoods bargainGoods = getBargainGoodsById(bargainActivity.getBargainGoodsId());
        
        Long helpCount = bargainRecordMapper.selectCount(
                new LambdaQueryWrapper<BargainRecord>()
                        .eq(BargainRecord::getHelpUserId, helpUserId)
                        .eq(BargainRecord::getBargainActivityId, bargainActivityId)
        );
        if (helpCount > 0) {
            throw new BusinessException("您已帮该用户砍过价");
        }
        
        if (bargainGoods.getLimitHelp() > 0) {
            Long userHelpCount = bargainRecordMapper.selectCount(
                    new LambdaQueryWrapper<BargainRecord>()
                            .eq(BargainRecord::getHelpUserId, helpUserId)
            );
            if (userHelpCount >= bargainGoods.getLimitHelp()) {
                throw new BusinessException("您今日帮砍次数已达上限");
            }
        }
        
        SysUser helper = null;
        if (helpUserId != null) {
            helper = sysUserMapper.selectById(helpUserId);
        }
        
        BigDecimal minBargain = bargainGoods.getMinBargain();
        BigDecimal maxBargain = bargainGoods.getMaxBargain();
        
        BigDecimal remainingAmount = bargainActivity.getCurrentPrice().subtract(bargainGoods.getBargainPrice());
        if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("该商品已砍到最低价");
        }
        
        if (remainingAmount.compareTo(minBargain) < 0) {
            maxBargain = remainingAmount;
            minBargain = remainingAmount;
        }
        
        if (maxBargain.compareTo(minBargain) < 0) {
            maxBargain = minBargain;
        }
        
        double random = Math.random();
        BigDecimal bargainAmount = minBargain.add(
                maxBargain.subtract(minBargain).multiply(BigDecimal.valueOf(random))
        );
        bargainAmount = bargainAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        
        if (bargainActivity.getCurrentPrice().subtract(bargainAmount).compareTo(bargainGoods.getBargainPrice()) < 0) {
            bargainAmount = bargainActivity.getCurrentPrice().subtract(bargainGoods.getBargainPrice());
        }
        
        BigDecimal newCurrentPrice = bargainActivity.getCurrentPrice().subtract(bargainAmount);
        BigDecimal newBargainAmount = bargainActivity.getBargainAmount().add(bargainAmount);
        int newBargainCount = bargainActivity.getBargainCount() + 1;
        
        bargainActivityMapper.update(null,
                new LambdaUpdateWrapper<BargainActivity>()
                        .eq(BargainActivity::getId, bargainActivityId)
                        .set(BargainActivity::getCurrentPrice, newCurrentPrice)
                        .set(BargainActivity::getBargainAmount, newBargainAmount)
                        .set(BargainActivity::getBargainCount, newBargainCount)
        );
        
        if (newCurrentPrice.compareTo(bargainGoods.getBargainPrice()) <= 0) {
            bargainActivityMapper.update(null,
                    new LambdaUpdateWrapper<BargainActivity>()
                            .eq(BargainActivity::getId, bargainActivityId)
                            .set(BargainActivity::getStatus, Constants.BargainActivityStatus.COMPLETED)
            );
        }
        
        BargainRecord record = new BargainRecord();
        record.setBargainActivityId(bargainActivityId);
        record.setHelpUserId(helpUserId);
        record.setHelpUserNickname(helper != null ? helper.getNickname() : "微信用户");
        record.setHelpUserAvatar(helper != null ? helper.getAvatar() : "");
        record.setBargainAmount(bargainAmount);
        record.setIsNewUser(Constants.DefaultFlag.NO);
        record.setBargainTime(now);
        
        bargainRecordMapper.insert(record);
        
        return record;
    }

    @Override
    public BargainActivity getBargainActivityById(Long id) {
        BargainActivity bargainActivity = bargainActivityMapper.selectById(id);
        if (bargainActivity == null) {
            throw new BusinessException("砍价活动不存在");
        }
        return bargainActivity;
    }

    @Override
    public Page<BargainActivity> getMyBargainActivities(Long userId, Integer pageNum, Integer pageSize, Integer status) {
        Page<BargainActivity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BargainActivity> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(BargainActivity::getUserId, userId);
        
        if (status != null) {
            wrapper.eq(BargainActivity::getStatus, status);
        }
        
        wrapper.orderByDesc(BargainActivity::getCreateTime);
        
        return bargainActivityMapper.selectPage(page, wrapper);
    }

    @Override
    public List<BargainRecord> getBargainRecords(Long bargainActivityId) {
        return bargainRecordMapper.selectList(
                new LambdaQueryWrapper<BargainRecord>()
                        .eq(BargainRecord::getBargainActivityId, bargainActivityId)
                        .orderByDesc(BargainRecord::getBargainTime)
        );
    }

    @Override
    public Page<BargainGoods> getRecommendBargains(Integer pageNum, Integer pageSize) {
        return getBargainGoodsPage(pageNum, pageSize, null, 1);
    }

    @Override
    public Page<BargainGoods> getHotBargains(Integer pageNum, Integer pageSize) {
        return getBargainGoodsPage(pageNum, pageSize, 1, null);
    }
}
