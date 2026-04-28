package com.dj.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.PromotionPoster;
import com.dj.mall.entity.PromotionStat;
import com.dj.mall.entity.SysUser;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.PromotionPosterMapper;
import com.dj.mall.mapper.PromotionStatMapper;
import com.dj.mall.mapper.SysUserMapper;
import com.dj.mall.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionPosterMapper promotionPosterMapper;

    @Autowired
    private PromotionStatMapper promotionStatMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<PromotionPoster> getPosterList(Integer posterType) {
        LambdaQueryWrapper<PromotionPoster> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PromotionPoster::getStatus, Constants.Status.ENABLE)
                .eq(PromotionPoster::getDeleted, 0);
        
        if (posterType != null) {
            wrapper.eq(PromotionPoster::getPosterType, posterType);
        }
        
        wrapper.orderByAsc(PromotionPoster::getSort)
                .orderByDesc(PromotionPoster::getCreateTime);
        
        return promotionPosterMapper.selectList(wrapper);
    }

    @Override
    public PromotionPoster getPosterById(Long id) {
        PromotionPoster poster = promotionPosterMapper.selectById(id);
        if (poster == null || poster.getDeleted() == 1) {
            throw new BusinessException("海报不存在");
        }
        if (!Constants.Status.ENABLE.equals(poster.getStatus())) {
            throw new BusinessException("海报已禁用");
        }
        return poster;
    }

    @Override
    public Page<PromotionStat> getPromotionStatPage(Long userId, Integer pageNum, Integer pageSize) {
        Page<PromotionStat> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PromotionStat> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PromotionStat::getUserId, userId)
                .orderByDesc(PromotionStat::getStatDate);
        return promotionStatMapper.selectPage(page, wrapper);
    }

    @Override
    public Map<String, Object> getPromotionSummary(Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        List<PromotionStat> stats = promotionStatMapper.selectList(
                new LambdaQueryWrapper<PromotionStat>()
                        .eq(PromotionStat::getUserId, userId)
                        .orderByDesc(PromotionStat::getStatDate)
        );
        
        int totalPv = 0;
        int totalUv = 0;
        int totalRegister = 0;
        int totalOrder = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalCommission = BigDecimal.ZERO;
        
        for (PromotionStat stat : stats) {
            totalPv += stat.getPvCount();
            totalUv += stat.getUvCount();
            totalRegister += stat.getRegisterCount();
            totalOrder += stat.getOrderCount();
            totalAmount = totalAmount.add(stat.getOrderAmount());
            totalCommission = totalCommission.add(stat.getCommissionAmount());
        }
        
        result.put("totalPv", totalPv);
        result.put("totalUv", totalUv);
        result.put("totalRegister", totalRegister);
        result.put("totalOrder", totalOrder);
        result.put("totalAmount", totalAmount);
        result.put("totalCommission", totalCommission);
        result.put("todayData", stats.isEmpty() ? null : stats.get(0));
        
        return result;
    }

    @Override
    public Page<Map<String, Object>> getPromotionRank(Integer pageNum, Integer pageSize) {
        Page<Map<String, Object>> resultPage = new Page<>(pageNum, pageSize);
        
        List<Map<String, Object>> rankList = new ArrayList<>();
        
        List<SysUser> users = sysUserMapper.selectList(
                new LambdaQueryWrapper<SysUser>()
                        .gt(SysUser::getParentId, 0)
                        .eq(SysUser::getStatus, Constants.UserStatus.NORMAL)
                        .eq(SysUser::getDeleted, 0)
                        .last("LIMIT 100")
        );
        
        for (SysUser user : users) {
            Map<String, Object> item = new HashMap<>();
            item.put("userId", user.getId());
            item.put("nickname", user.getNickname());
            item.put("avatar", user.getAvatar());
            
            List<SysUser> subordinates = sysUserMapper.selectList(
                    new LambdaQueryWrapper<SysUser>()
                            .eq(SysUser::getParentId, user.getId())
                            .eq(SysUser::getDeleted, 0)
            );
            
            item.put("subordinateCount", subordinates.size());
            
            List<PromotionStat> stats = promotionStatMapper.selectList(
                    new LambdaQueryWrapper<PromotionStat>()
                            .eq(PromotionStat::getUserId, user.getId())
            );
            
            int totalOrder = 0;
            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal totalCommission = BigDecimal.ZERO;
            
            for (PromotionStat stat : stats) {
                totalOrder += stat.getOrderCount();
                totalAmount = totalAmount.add(stat.getOrderAmount());
                totalCommission = totalCommission.add(stat.getCommissionAmount());
            }
            
            item.put("totalOrder", totalOrder);
            item.put("totalAmount", totalAmount);
            item.put("totalCommission", totalCommission);
            
            rankList.add(item);
        }
        
        rankList.sort((a, b) -> {
            BigDecimal amountA = (BigDecimal) a.getOrDefault("totalAmount", BigDecimal.ZERO);
            BigDecimal amountB = (BigDecimal) b.getOrDefault("totalAmount", BigDecimal.ZERO);
            return amountB.compareTo(amountA);
        });
        
        int start = (int) ((pageNum - 1) * pageSize);
        int end = Math.min(start + pageSize, rankList.size());
        
        List<Map<String, Object>> pageList = start < rankList.size() ? rankList.subList(start, end) : new ArrayList<>();
        
        resultPage.setRecords(pageList);
        resultPage.setTotal(rankList.size());
        
        return resultPage;
    }

    @Override
    public Page<Map<String, Object>> getPromotionUserList(Long userId, Integer pageNum, Integer pageSize) {
        Page<Map<String, Object>> resultPage = new Page<>(pageNum, pageSize);
        
        List<SysUser> subordinates = sysUserMapper.selectList(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getParentId, userId)
                        .eq(SysUser::getDeleted, 0)
                        .orderByDesc(SysUser::getCreateTime)
        );
        
        List<Map<String, Object>> resultList = new ArrayList<>();
        
        for (SysUser user : subordinates) {
            Map<String, Object> item = new HashMap<>();
            item.put("userId", user.getId());
            item.put("nickname", user.getNickname());
            item.put("avatar", user.getAvatar());
            item.put("phone", user.getPhone());
            item.put("registerTime", user.getCreateTime());
            
            List<PromotionStat> stats = promotionStatMapper.selectList(
                    new LambdaQueryWrapper<PromotionStat>()
                            .eq(PromotionStat::getUserId, user.getId())
            );
            
            int totalOrder = 0;
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            for (PromotionStat stat : stats) {
                totalOrder += stat.getOrderCount();
                totalAmount = totalAmount.add(stat.getOrderAmount());
            }
            
            item.put("orderCount", totalOrder);
            item.put("orderAmount", totalAmount);
            
            resultList.add(item);
        }
        
        int start = (int) ((pageNum - 1) * pageSize);
        int end = Math.min(start + pageSize, resultList.size());
        
        List<Map<String, Object>> pageList = start < resultList.size() ? resultList.subList(start, end) : new ArrayList<>();
        
        resultPage.setRecords(pageList);
        resultPage.setTotal(resultList.size());
        
        return resultPage;
    }

    @Override
    public void recordPromotionView(Long userId, Long viewerId) {
        SysUser promoter = sysUserMapper.selectById(userId);
        if (promoter == null || promoter.getDeleted() == 1) {
            return;
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = calendar.getTime();
        
        PromotionStat stat = promotionStatMapper.selectOne(
                new LambdaQueryWrapper<PromotionStat>()
                        .eq(PromotionStat::getUserId, userId)
                        .eq(PromotionStat::getStatDate, today)
        );
        
        if (stat == null) {
            stat = new PromotionStat();
            stat.setUserId(userId);
            stat.setStatDate(today);
            stat.setPvCount(1);
            stat.setUvCount(viewerId != null ? 1 : 0);
            stat.setRegisterCount(0);
            stat.setOrderCount(0);
            stat.setOrderAmount(BigDecimal.ZERO);
            stat.setCommissionAmount(BigDecimal.ZERO);
            promotionStatMapper.insert(stat);
        } else {
            promotionStatMapper.update(null,
                    new LambdaUpdateWrapper<PromotionStat>()
                            .eq(PromotionStat::getId, stat.getId())
                            .setSql("pv_count = pv_count + 1")
            );
            
            if (viewerId != null) {
                promotionStatMapper.update(null,
                        new LambdaUpdateWrapper<PromotionStat>()
                                .eq(PromotionStat::getId, stat.getId())
                                .setSql("uv_count = uv_count + 1")
                );
            }
        }
    }
}
