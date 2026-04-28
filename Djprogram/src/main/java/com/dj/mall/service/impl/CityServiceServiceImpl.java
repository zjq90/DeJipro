package com.dj.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.CityService;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.CityServiceMapper;
import com.dj.mall.service.CityServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CityServiceServiceImpl implements CityServiceService {

    @Autowired
    private CityServiceMapper cityServiceMapper;

    @Override
    public List<CityService> getServiceList(Long parentId) {
        if (parentId == null) {
            parentId = 0L;
        }
        
        return cityServiceMapper.selectList(
                new LambdaQueryWrapper<CityService>()
                        .eq(CityService::getParentId, parentId)
                        .eq(CityService::getStatus, Constants.Status.ENABLE)
                        .eq(CityService::getDeleted, 0)
                        .orderByAsc(CityService::getSort)
        );
    }

    @Override
    public CityService getServiceById(Long id) {
        CityService service = cityServiceMapper.selectById(id);
        if (service == null || service.getDeleted() == 1) {
            throw new BusinessException("服务不存在");
        }
        if (!Constants.Status.ENABLE.equals(service.getStatus())) {
            throw new BusinessException("服务已禁用");
        }
        return service;
    }

    @Override
    public List<CityService> getServiceTree() {
        List<CityService> allServices = cityServiceMapper.selectList(
                new LambdaQueryWrapper<CityService>()
                        .eq(CityService::getStatus, Constants.Status.ENABLE)
                        .eq(CityService::getDeleted, 0)
                        .orderByAsc(CityService::getSort)
        );
        
        Map<Long, List<CityService>> parentMap = allServices.stream()
                .collect(Collectors.groupingBy(CityService::getParentId));
        
        List<CityService> rootServices = parentMap.getOrDefault(0L, new ArrayList<>());
        
        buildServiceTree(rootServices, parentMap);
        
        return rootServices;
    }

    private void buildServiceTree(List<CityService> services, Map<Long, List<CityService>> parentMap) {
        for (CityService service : services) {
            List<CityService> children = parentMap.get(service.getId());
            if (children != null && !children.isEmpty()) {
                buildServiceTree(children, parentMap);
            }
        }
    }
}
