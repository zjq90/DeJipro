package com.dj.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.entity.SysUser;
import com.dj.mall.entity.UserAuth;
import com.dj.mall.entity.UserLevel;

import java.util.List;
import java.util.Map;

public interface UserService {

    SysUser getById(Long userId);

    SysUser getByPhone(String phone);

    SysUser getByUsername(String username);

    void updateById(SysUser user);

    UserAuth getUserAuth(Long userId);

    void submitAuth(UserAuth auth);

    UserLevel getUserLevel(Long levelId);

    List<UserLevel> getAllLevels();

    Page<SysUser> getTeamList(Long userId, Integer pageNum, Integer pageSize);

    Map<String, Object> getUserInfo(Long userId);

    void updatePassword(Long userId, String oldPassword, String newPassword);

    void updatePhone(Long userId, String phone, String code);

    String generateInviteCode();

    SysUser getByInviteCode(String inviteCode);
}
