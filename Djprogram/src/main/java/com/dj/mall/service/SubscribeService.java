package com.dj.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.entity.SubscribeTemplate;
import com.dj.mall.entity.UserSubscribe;

import java.util.List;

public interface SubscribeService {

    Page<SubscribeTemplate> getTemplatePage(Integer pageNum, Integer pageSize);

    List<SubscribeTemplate> getTemplateList();

    SubscribeTemplate getTemplateById(Long id);

    SubscribeTemplate getTemplateByCode(String templateCode);

    UserSubscribe subscribe(Long userId, Long templateId);

    List<UserSubscribe> getUserSubscribes(Long userId);

    void useSubscribe(Long userId, String templateCode);
}
