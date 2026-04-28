package com.dj.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.Coupon;
import com.dj.mall.entity.UserCoupon;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.CouponMapper;
import com.dj.mall.mapper.UserCouponMapper;
import com.dj.mall.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Override
    public Page<Coupon> getCouponPage(Integer pageNum, Integer pageSize, Integer couponType, Integer status) {
        Page<Coupon> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(Coupon::getDeleted, 0);
        
        if (couponType != null) {
            wrapper.eq(Coupon::getCouponType, couponType);
        }
        
        if (status != null) {
            wrapper.eq(Coupon::getStatus, status);
        }
        
        wrapper.orderByAsc(Coupon::getSort)
                .orderByDesc(Coupon::getCreateTime);
        
        return couponMapper.selectPage(page, wrapper);
    }

    @Override
    public Coupon getCouponById(Long id) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null || coupon.getDeleted() == 1) {
            throw new BusinessException("优惠券不存在");
        }
        return coupon;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCoupon receiveCoupon(Long userId, Long couponId) {
        Coupon coupon = getCouponById(couponId);
        
        if (!Constants.Status.ENABLE.equals(coupon.getStatus())) {
            throw new BusinessException("优惠券已下架");
        }
        
        Date now = new Date();
        if (coupon.getReceiveStartTime() != null && now.before(coupon.getReceiveStartTime())) {
            throw new BusinessException("优惠券未开始领取");
        }
        if (coupon.getReceiveEndTime() != null && now.after(coupon.getReceiveEndTime())) {
            throw new BusinessException("优惠券已结束领取");
        }
        
        if (coupon.getTotalCount() > 0 && coupon.getReceiveCount() >= coupon.getTotalCount()) {
            throw new BusinessException("优惠券已被领取完");
        }
        
        if (coupon.getPerLimit() > 0) {
            Long receivedCount = userCouponMapper.selectCount(
                    new LambdaQueryWrapper<UserCoupon>()
                            .eq(UserCoupon::getUserId, userId)
                            .eq(UserCoupon::getCouponId, couponId)
            );
            if (receivedCount >= coupon.getPerLimit()) {
                throw new BusinessException("您已达到领取上限");
            }
        }
        
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setCouponName(coupon.getCouponName());
        userCoupon.setCouponType(coupon.getCouponType());
        userCoupon.setDiscountType(coupon.getDiscountType());
        userCoupon.setDiscountAmount(coupon.getDiscountAmount());
        userCoupon.setMinAmount(coupon.getMinAmount());
        userCoupon.setStatus(Constants.UserCouponStatus.UNUSED);
        userCoupon.setReceiveTime(now);
        
        if (coupon.getValidDays() != null && coupon.getValidDays() > 0) {
            userCoupon.setValidStartTime(now);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_MONTH, coupon.getValidDays());
            userCoupon.setValidEndTime(calendar.getTime());
        } else {
            userCoupon.setValidStartTime(coupon.getValidStartTime());
            userCoupon.setValidEndTime(coupon.getValidEndTime());
        }
        
        userCouponMapper.insert(userCoupon);
        
        couponMapper.update(null,
                new LambdaUpdateWrapper<Coupon>()
                        .eq(Coupon::getId, couponId)
                        .setSql("receive_count = receive_count + 1")
        );
        
        return userCoupon;
    }

    @Override
    public Page<UserCoupon> getUserCouponPage(Long userId, Integer pageNum, Integer pageSize, Integer status) {
        Page<UserCoupon> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(UserCoupon::getUserId, userId);
        
        if (status != null) {
            wrapper.eq(UserCoupon::getStatus, status);
        }
        
        wrapper.orderByDesc(UserCoupon::getReceiveTime);
        
        return userCouponMapper.selectPage(page, wrapper);
    }

    @Override
    public List<UserCoupon> getAvailableCoupons(Long userId, Long goodsId, BigDecimal amount) {
        Date now = new Date();
        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getStatus, Constants.UserCouponStatus.UNUSED)
                .le(UserCoupon::getValidStartTime, now)
                .ge(UserCoupon::getValidEndTime, now);
        
        List<UserCoupon> coupons = userCouponMapper.selectList(wrapper);
        
        coupons.removeIf(coupon -> {
            if (amount != null && coupon.getMinAmount() != null && amount.compareTo(coupon.getMinAmount()) < 0) {
                return true;
            }
            return false;
        });
        
        return coupons;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void useCoupon(Long userCouponId, Long orderId) {
        UserCoupon userCoupon = userCouponMapper.selectById(userCouponId);
        if (userCoupon == null) {
            throw new BusinessException("优惠券不存在");
        }
        
        if (!Constants.UserCouponStatus.UNUSED.equals(userCoupon.getStatus())) {
            throw new BusinessException("优惠券已使用或已过期");
        }
        
        Date now = new Date();
        if (userCoupon.getValidStartTime() != null && now.before(userCoupon.getValidStartTime())) {
            throw new BusinessException("优惠券未生效");
        }
        if (userCoupon.getValidEndTime() != null && now.after(userCoupon.getValidEndTime())) {
            throw new BusinessException("优惠券已过期");
        }
        
        userCouponMapper.update(null,
                new LambdaUpdateWrapper<UserCoupon>()
                        .eq(UserCoupon::getId, userCouponId)
                        .set(UserCoupon::getStatus, Constants.UserCouponStatus.USED)
                        .set(UserCoupon::getOrderId, orderId)
                        .set(UserCoupon::getUseTime, now)
        );
        
        couponMapper.update(null,
                new LambdaUpdateWrapper<Coupon>()
                        .eq(Coupon::getId, userCoupon.getCouponId())
                        .setSql("use_count = use_count + 1")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnCoupon(Long userCouponId) {
        UserCoupon userCoupon = userCouponMapper.selectById(userCouponId);
        if (userCoupon == null) {
            return;
        }
        
        if (!Constants.UserCouponStatus.USED.equals(userCoupon.getStatus())) {
            return;
        }
        
        userCouponMapper.update(null,
                new LambdaUpdateWrapper<UserCoupon>()
                        .eq(UserCoupon::getId, userCouponId)
                        .set(UserCoupon::getStatus, Constants.UserCouponStatus.EXPIRED)
        );
    }
}
