package com.dj.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.entity.*;

import java.util.List;
import java.util.Map;

public interface AssetService {

    UserAsset getUserAsset(Long userId);

    Page<BalanceRecord> getBalanceRecordList(Long userId, Integer pageNum, Integer pageSize, Integer type);

    Page<CommissionRecord> getCommissionRecordList(Long userId, Integer pageNum, Integer pageSize, Integer status);

    Page<IntegralRecord> getIntegralRecordList(Long userId, Integer pageNum, Integer pageSize, Integer type);

    List<Map<String, Object>> getCommissionRank(Integer limit);

    Map<String, Object> getAssetStatistics(Long userId);
}
