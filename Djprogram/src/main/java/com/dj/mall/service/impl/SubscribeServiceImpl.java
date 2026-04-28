package com.dj.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.SubscribeTemplate;
import com.dj.mall.entity.UserSubscribe;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.SubscribeTemplateMapper;
import com.dj.mall.mapper.UserSubscribeMapper;
import com.dj.mall.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SubscribeServiceImpl implements SubscribeService {

    @Autowired
    private SubscribeTemplateMapper subscribeTemplateMapper;

    @Autowired
    private UserSubscribeMapper userSubscribeMapper;

    @Override
    public Page<SubscribeTemplate> getTemplatePage(Integer pageNum, Integer pageSize) {
        Page<SubscribeTemplate> page = new Page<>(pageNum, pageSize);
        return subscribeTemplateMapper.selectPage(page,
                new LambdaQueryWrapper<SubscribeTemplate>()
                        .eq(SubscribeTemplate::getStatus, Constants.Status.ENABLE)
                        .eq(SubscribeTemplate::getDeleted, 0)
                        .orderByAsc(SubscribeTemplate::getSort)
                        .orderByDesc(SubscribeTemplate::getCreateTime)
        );
    }

    @Override
    public List<SubscribeTemplate> getTemplateList() {
        return subscribeTemplateMapper.selectList(
                new LambdaQueryWrapper<SubscribeTemplate>()
                        .eq(SubscribeTemplate::getStatus, Constants.Status.ENABLE)
                        .eq(SubscribeTemplate::getDeleted, 0)
                        .orderByAsc(SubscribeTemplate::getSort)
        );
    }

    @Override
    public SubscribeTemplate getTemplateById(Long id) {
        SubscribeTemplate template = subscribeTemplateMapper.selectById(id);
        if (template == null || template.getDeleted() == 1) {
            throw new BusinessException("模板不存在");
        }
        return template;
    }

    @Override
    public SubscribeTemplate getTemplateByCode(String templateCode) {
        return subscribeTemplateMapper.selectOne(
                new LambdaQueryWrapper<SubscribeTemplate>()
                        .eq(SubscribeTemplate::getTemplateCode, templateCode)
                        .eq(SubscribeTemplate::getStatus, Constants.Status.ENABLE)
                        .eq(SubscribeTemplate::getDeleted, 0)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserSubscribe subscribe(Long userId, Long templateId) {
        SubscribeTemplate template = getTemplateById(templateId);
        
        if (!Constants.Status.ENABLE.equals(template.getStatus())) {
            throw new BusinessException("模板已禁用");
        }
        
        UserSubscribe existing = userSubscribeMapper.selectOne(
                new LambdaQueryWrapper<UserSubscribe>()
                        .eq(UserSubscribe::getUserId, userId)
                        .eq(UserSubscribe::getTemplateId, templateId)
                        .eq(UserSubscribe::getStatus, Constants.UserSubscribeStatus.ACTIVE)
        );
        
        if (existing != null && Constants.SubscribeType.LONG_TERM.equals(template.getTemplateType())) {
            return existing;
        }
        
        UserSubscribe userSubscribe = new UserSubscribe();
        userSubscribe.setUserId(userId);
        userSubscribe.setTemplateId(templateId);
        userSubscribe.setTemplateCode(template.getTemplateCode());
        userSubscribe.setSubscribeType(template.getTemplateType());
        userSubscribe.setSubscribeTime(new Date());
        userSubscribe.setUsedCount(0);
        userSubscribe.setMaxCount(Constants.SubscribeType.ONCE.equals(template.getTemplateType()) ? 1 : -1);
        userSubscribe.setStatus(Constants.UserSubscribeStatus.ACTIVE);
        
        userSubscribeMapper.insert(userSubscribe);
        
        return userSubscribe;
    }

    @Override
    public List<UserSubscribe> getUserSubscribes(Long userId) {
        Date now = new Date();
        return userSubscribeMapper.selectList(
                new LambdaQueryWrapper<UserSubscribe>()
                        .eq(UserSubscribe::getUserId, userId)
                        .eq(UserSubscribe::getStatus, Constants.UserSubscribeStatus.ACTIVE)
                        .and(w -> w.isNull(UserSubscribe::getExpireTime)
                                .or().ge(UserSubscribe::getExpireTime, now))
                        .orderByDesc(UserSubscribe::getSubscribeTime)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void useSubscribe(Long userId, String templateCode) {
        UserSubscribe userSubscribe = userSubscribeMapper.selectOne(
                new LambdaQueryWrapper<UserSubscribe>()
                        .eq(UserSubscribe::getUserId, userId)
                        .eq(UserSubscribe::getTemplateCode, templateCode)
                        .eq(UserSubscribe::getStatus, Constants.UserSubscribeStatus.ACTIVE)
                        .orderByDesc(UserSubscribe::getSubscribeTime)
                        .last("LIMIT 1")
        );
        
        if (userSubscribe == null) {
            return;
        }
        
        if (userSubscribe.getMaxCount() > 0) {
            int newUsedCount = userSubscribe.getUsedCount() + 1;
            if (newUsedCount >= userSubscribe.getMaxCount()) {
                userSubscribeMapper.update(null,
                        new LambdaUpdateWrapper<UserSubscribe>()
                                .eq(UserSubscribe::getId, userSubscribe.getId())
                                .set(UserSubscribe::getStatus, Constants.UserSubscribeStatus.EXPIRED)
                                .set(UserSubscribe::getUsedCount, newUsedCount)
                );
            } else {
                userSubscribeMapper.update(null,
                        new LambdaUpdateWrapper<UserSubscribe>()
                                .eq(UserSubscribe::getId, userSubscribe.getId())
                                .set(UserSubscribe::getUsedCount, newUsedCount)
                );
            }
        }
    }
}
