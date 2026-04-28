package com.dj.mall.service;

import com.dj.mall.entity.CityService;

import java.util.List;

public interface CityServiceService {

    List<CityService> getServiceList(Long parentId);

    CityService getServiceById(Long id);

    List<CityService> getServiceTree();
}
