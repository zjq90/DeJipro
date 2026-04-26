package com.dj.mall.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.*;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.*;
import com.dj.mall.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private UserLevelMapper userLevelMapper;

    @Autowired
    private UserAssetMapper userAssetMapper;

    @Override
    public SysUser getById(Long userId) {
        return sysUserMapper.selectById(userId);
    }

    @Override
    public SysUser getByPhone(String phone) {
        return sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getPhone, phone)
                        .eq(SysUser::getDeleted, 0)
        );
    }

    @Override
    public SysUser getByUsername(String username) {
        return sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)
                        .eq(SysUser::getDeleted, 0)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(SysUser user) {
        sysUserMapper.updateById(user);
    }

    @Override
    public UserAuth getUserAuth(Long userId) {
        return userAuthMapper.selectOne(
                new LambdaQueryWrapper<UserAuth>()
                        .eq(UserAuth::getUserId, userId)
                        .eq(UserAuth::getDeleted, 0)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitAuth(UserAuth auth) {
        UserAuth existAuth = userAuthMapper.selectOne(
                new LambdaQueryWrapper<UserAuth>()
                        .eq(UserAuth::getUserId, auth.getUserId())
                        .eq(UserAuth::getDeleted, 0)
        );

        if (existAuth != null) {
            if (existAuth.getStatus() == Constants.AuthStatus.PENDING) {
                throw new BusinessException("实名认证正在审核中，请勿重复提交");
            }
            if (existAuth.getStatus() == Constants.AuthStatus.PASS) {
                throw new BusinessException("已完成实名认证");
            }
            auth.setId(existAuth.getId());
            auth.setStatus(Constants.AuthStatus.PENDING);
            userAuthMapper.updateById(auth);
        } else {
            auth.setStatus(Constants.AuthStatus.PENDING);
            userAuthMapper.insert(auth);
        }
    }

    @Override
    public UserLevel getUserLevel(Long levelId) {
        return userLevelMapper.selectById(levelId);
    }

    @Override
    public List<UserLevel> getAllLevels() {
        return userLevelMapper.selectList(
                new LambdaQueryWrapper<UserLevel>()
                        .eq(UserLevel::getDeleted, 0)
                        .orderByAsc(UserLevel::getLevelValue)
        );
    }

    @Override
    public Page<SysUser> getTeamList(Long userId, Integer pageNum, Integer pageSize) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        return sysUserMapper.selectPage(page,
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getParentId, userId)
                        .eq(SysUser::getDeleted, 0)
                        .orderByDesc(SysUser::getCreateTime)
        );
    }

    @Override
    public Map<String, Object> getUserInfo(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserAsset asset = userAssetMapper.selectOne(
                new LambdaQueryWrapper<UserAsset>()
                        .eq(UserAsset::getUserId, userId)
        );

        UserLevel level = userLevelMapper.selectById(user.getLevelId());

        UserAuth auth = userAuthMapper.selectOne(
                new LambdaQueryWrapper<UserAuth>()
                        .eq(UserAuth::getUserId, userId)
                        .eq(UserAuth::getDeleted, 0)
        );

        Long teamCount = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getParentId, userId)
                        .eq(SysUser::getDeleted, 0)
        );

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("asset", asset);
        result.put("level", level);
        result.put("auth", auth);
        result.put("teamCount", teamCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        sysUserMapper.update(null,
                new LambdaUpdateWrapper<SysUser>()
                        .eq(SysUser::getId, userId)
                        .set(SysUser::getPassword, BCrypt.hashpw(newPassword))
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePhone(Long userId, String phone, String code) {
        SysUser existUser = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getPhone, phone)
                        .eq(SysUser::getDeleted, 0)
                        .ne(SysUser::getId, userId)
        );
        if (existUser != null) {
            throw new BusinessException("该手机号已被绑定");
        }

        sysUserMapper.update(null,
                new LambdaUpdateWrapper<SysUser>()
                        .eq(SysUser::getId, userId)
                        .set(SysUser::getPhone, phone)
        );
    }

    @Override
    public String generateInviteCode() {
        String code;
        int maxRetry = 10;
        int retry = 0;
        do {
            code = RandomUtil.randomStringUpper(6);
            Long count = sysUserMapper.selectCount(
                    new LambdaQueryWrapper<SysUser>()
                            .eq(SysUser::getInviteCode, code)
            );
            if (count == 0) {
                return code;
            }
            retry++;
        } while (retry < maxRetry);
        
        return IdUtil.getSnowflakeNextIdStr();
    }

    @Override
    public SysUser getByInviteCode(String inviteCode) {
        return sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getInviteCode, inviteCode)
                        .eq(SysUser::getDeleted, 0)
        );
    }
}
