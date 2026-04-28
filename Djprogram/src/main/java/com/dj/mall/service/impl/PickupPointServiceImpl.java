package com.dj.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.PickupPoint;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.PickupPointMapper;
import com.dj.mall.service.PickupPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PickupPointServiceImpl implements PickupPointService {

    @Autowired
    private PickupPointMapper pickupPointMapper;

    private static final double EARTH_RADIUS = 6371.0;

    @Override
    public List<PickupPoint> getNearbyPoints(BigDecimal longitude, BigDecimal latitude, BigDecimal radius, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        
        List<PickupPoint> allPoints = pickupPointMapper.selectList(
                new LambdaQueryWrapper<PickupPoint>()
                        .eq(PickupPoint::getStatus, Constants.Status.ENABLE)
                        .eq(PickupPoint::getDeleted, 0)
        );
        
        if (longitude == null || latitude == null) {
            return allPoints.subList(0, Math.min(limit, allPoints.size()));
        }
        
        List<PickupPointWithDistance> pointsWithDistance = new ArrayList<>();
        
        for (PickupPoint point : allPoints) {
            if (point.getLongitude() == null || point.getLatitude() == null) {
                continue;
            }
            
            double distance = calculateDistance(
                    longitude.doubleValue(), latitude.doubleValue(),
                    point.getLongitude().doubleValue(), point.getLatitude().doubleValue()
            );
            
            if (radius == null || distance <= radius.doubleValue()) {
                pointsWithDistance.add(new PickupPointWithDistance(point, distance));
            }
        }
        
        pointsWithDistance.sort(Comparator.comparingDouble(PickupPointWithDistance::getDistance));
        
        List<PickupPoint> result = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, pointsWithDistance.size()); i++) {
            result.add(pointsWithDistance.get(i).getPoint());
        }
        
        return result;
    }

    private double calculateDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double radLon1 = Math.toRadians(lon1);
        double radLon2 = Math.toRadians(lon2);
        
        double dLat = radLat2 - radLat1;
        double dLon = radLon2 - radLon1;
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(radLat1) * Math.cos(radLat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS * c;
    }

    @Override
    public Page<PickupPoint> getPickupPointPage(Integer pageNum, Integer pageSize, String province, String city, String district) {
        Page<PickupPoint> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PickupPoint> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(PickupPoint::getStatus, Constants.Status.ENABLE)
                .eq(PickupPoint::getDeleted, 0);
        
        if (province != null && !province.isEmpty()) {
            wrapper.eq(PickupPoint::getProvince, province);
        }
        
        if (city != null && !city.isEmpty()) {
            wrapper.eq(PickupPoint::getCity, city);
        }
        
        if (district != null && !district.isEmpty()) {
            wrapper.eq(PickupPoint::getDistrict, district);
        }
        
        wrapper.orderByAsc(PickupPoint::getSort)
                .orderByDesc(PickupPoint::getCreateTime);
        
        return pickupPointMapper.selectPage(page, wrapper);
    }

    @Override
    public PickupPoint getPickupPointById(Long id) {
        PickupPoint point = pickupPointMapper.selectById(id);
        if (point == null || point.getDeleted() == 1) {
            throw new BusinessException("提货点不存在");
        }
        if (!Constants.Status.ENABLE.equals(point.getStatus())) {
            throw new BusinessException("提货点已禁用");
        }
        return point;
    }

    @Override
    public List<PickupPoint> getPointsByCity(String city) {
        return pickupPointMapper.selectList(
                new LambdaQueryWrapper<PickupPoint>()
                        .eq(PickupPoint::getCity, city)
                        .eq(PickupPoint::getStatus, Constants.Status.ENABLE)
                        .eq(PickupPoint::getDeleted, 0)
                        .orderByAsc(PickupPoint::getSort)
        );
    }

    private static class PickupPointWithDistance {
        private PickupPoint point;
        private double distance;

        public PickupPointWithDistance(PickupPoint point, double distance) {
            this.point = point;
            this.distance = distance;
        }

        public PickupPoint getPoint() {
            return point;
        }

        public double getDistance() {
            return distance;
        }
    }
}
