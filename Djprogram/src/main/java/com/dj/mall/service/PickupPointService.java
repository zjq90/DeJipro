package com.dj.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.entity.PickupPoint;

import java.math.BigDecimal;
import java.util.List;

public interface PickupPointService {

    List<PickupPoint> getNearbyPoints(BigDecimal longitude, BigDecimal latitude, BigDecimal radius, Integer limit);

    Page<PickupPoint> getPickupPointPage(Integer pageNum, Integer pageSize, String province, String city, String district);

    PickupPoint getPickupPointById(Long id);

    List<PickupPoint> getPointsByCity(String city);
}
