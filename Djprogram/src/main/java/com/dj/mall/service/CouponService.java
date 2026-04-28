package com.dj.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.entity.Coupon;
import com.dj.mall.entity.UserCoupon;

import java.util.List;

public interface CouponService {

    Page<Coupon> getCouponPage(Integer pageNum, Integer pageSize, Integer couponType, Integer status);

    Coupon getCouponById(Long id);

    UserCoupon receiveCoupon(Long userId, Long couponId);

    Page<UserCoupon> getUserCouponPage(Long userId, Integer pageNum, Integer pageSize, Integer status);

    List<UserCoupon> getAvailableCoupons(Long userId, Long goodsId, java.math.BigDecimal amount);

    void useCoupon(Long userCouponId, Long orderId);

    void returnCoupon(Long userCouponId);
}
