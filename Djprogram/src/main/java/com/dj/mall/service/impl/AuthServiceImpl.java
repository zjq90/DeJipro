package com.dj.mall.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dj.mall.common.Constants;
import com.dj.mall.dto.LoginDTO;
import com.dj.mall.dto.RegisterDTO;
import com.dj.mall.entity.SmsCode;
import com.dj.mall.entity.SysUser;
import com.dj.mall.entity.UserAsset;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.SmsCodeMapper;
import com.dj.mall.mapper.SysUserMapper;
import com.dj.mall.mapper.UserAssetMapper;
import com.dj.mall.service.AuthService;
import com.dj.mall.util.JwtUtil;
import com.dj.mall.util.RedisUtil;
import com.dj.mall.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SmsCodeMapper smsCodeMapper;

    @Autowired
    private UserAssetMapper userAssetMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO login(LoginDTO dto) {
        SysUser user = null;
        LoginVO vo = new LoginVO();
        vo.setIsNewUser(0);

        switch (dto.getLoginType()) {
            case 1:
                user = sysUserMapper.selectOne(
                        new LambdaQueryWrapper<SysUser>()
                                .eq(SysUser::getUsername, dto.getAccount())
                                .eq(SysUser::getDeleted, 0)
                );
                if (user == null) {
                    throw new BusinessException("用户名或密码错误");
                }
                if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
                    throw new BusinessException("用户名或密码错误");
                }
                break;

            case 2:
                user = sysUserMapper.selectOne(
                        new LambdaQueryWrapper<SysUser>()
                                .eq(SysUser::getPhone, dto.getAccount())
                                .eq(SysUser::getDeleted, 0)
                );
                if (user == null) {
                    throw new BusinessException("手机号或密码错误");
                }
                if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
                    throw new BusinessException("手机号或密码错误");
                }
                break;

            case 3:
                user = sysUserMapper.selectOne(
                        new LambdaQueryWrapper<SysUser>()
                                .eq(SysUser::getPhone, dto.getAccount())
                                .eq(SysUser::getDeleted, 0)
                );
                if (user == null) {
                    RegisterDTO registerDTO = new RegisterDTO();
                    registerDTO.setPhone(dto.getAccount());
                    registerDTO.setCode(dto.getPassword());
                    registerDTO.setInviteCode(dto.getInviteCode());
                    return register(registerDTO);
                }
                validateSmsCode(dto.getAccount(), dto.getPassword(), Constants.SmsCodeType.LOGIN);
                break;

            default:
                throw new BusinessException("不支持的登录类型");
        }

        if (user.getStatus() == Constants.Status.DISABLE) {
            throw new BusinessException("账号已被禁用");
        }
        if (user.getStatus() == Constants.UserStatus.CANCEL) {
            throw new BusinessException("账号已注销");
        }

        sysUserMapper.update(null,
                new LambdaUpdateWrapper<SysUser>()
                        .eq(SysUser::getId, user.getId())
                        .set(SysUser::getLastLoginTime, new Date())
                        .set(SysUser::getLastLoginIp, dto.getLoginIp())
        );

        String token = jwtUtil.generateToken(user.getId());
        redisUtil.set(Constants.TOKEN_PREFIX + user.getId(), token, Constants.TOKEN_EXPIRE, TimeUnit.SECONDS);

        BeanUtils.copyProperties(user, vo);
        vo.setToken(token);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO register(RegisterDTO dto) {
        validateSmsCode(dto.getPhone(), dto.getCode(), Constants.SmsCodeType.REGISTER);

        SysUser existUser = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getPhone, dto.getPhone())
                        .eq(SysUser::getDeleted, 0)
        );
        if (existUser != null) {
            throw new BusinessException("该手机号已注册");
        }

        SysUser user = new SysUser();
        user.setPhone(dto.getPhone());
        user.setUsername("U" + System.currentTimeMillis());
        if (dto.getPassword() != null) {
            user.setPassword(BCrypt.hashpw(dto.getPassword()));
        } else {
            user.setPassword(BCrypt.hashpw(Constants.DEFAULT_PASSWORD));
        }
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : "用户" + RandomUtil.randomNumbers(6));
        user.setStatus(Constants.UserStatus.NORMAL);
        user.setInviteCode(generateUniqueInviteCode());
        user.setLevelId(1L);

        if (dto.getInviteCode() != null && !dto.getInviteCode().isEmpty()) {
            SysUser parent = sysUserMapper.selectOne(
                    new LambdaQueryWrapper<SysUser>()
                            .eq(SysUser::getInviteCode, dto.getInviteCode())
                            .eq(SysUser::getDeleted, 0)
            );
            if (parent != null) {
                user.setParentId(parent.getId());
            }
        }

        sysUserMapper.insert(user);

        UserAsset asset = new UserAsset();
        asset.setUserId(user.getId());
        asset.setBalance(BigDecimal.ZERO);
        asset.setFrozenBalance(BigDecimal.ZERO);
        asset.setTotalCommission(BigDecimal.ZERO);
        asset.setAvailableCommission(BigDecimal.ZERO);
        asset.setTotalIntegral(0L);
        asset.setAvailableIntegral(0L);
        asset.setExperience(0L);
        userAssetMapper.insert(asset);

        smsCodeMapper.update(null,
                new LambdaUpdateWrapper<SmsCode>()
                        .eq(SmsCode::getPhone, dto.getPhone())
                        .eq(SmsCode::getType, Constants.SmsCodeType.REGISTER)
                        .set(SmsCode::getUsed, 1)
        );

        String token = jwtUtil.generateToken(user.getId());
        redisUtil.set(Constants.TOKEN_PREFIX + user.getId(), token, Constants.TOKEN_EXPIRE, TimeUnit.SECONDS);

        LoginVO vo = new LoginVO();
        BeanUtils.copyProperties(user, vo);
        vo.setToken(token);
        vo.setIsNewUser(1);
        return vo;
    }

    @Override
    public void logout(Long userId) {
        redisUtil.delete(Constants.TOKEN_PREFIX + userId);
    }

    @Override
    public void sendSmsCode(String phone, Integer type) {
        String key = Constants.SMS_CODE_PREFIX + type + ":" + phone;
        
        if (redisUtil.hasKey(key)) {
            throw new BusinessException("验证码发送过于频繁，请稍后再试");
        }

        String code = RandomUtil.randomNumbers(6);
        log.info("发送验证码: phone={}, type={}, code={}", phone, type, code);

        SmsCode smsCode = new SmsCode();
        smsCode.setPhone(phone);
        smsCode.setCode(code);
        smsCode.setType(type);
        smsCode.setUsed(0);
        smsCode.setExpireTime(new Date(System.currentTimeMillis() + Constants.SMS_CODE_EXPIRE * 60 * 1000L));
        smsCodeMapper.insert(smsCode);

        redisUtil.set(key, code, 60, TimeUnit.SECONDS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String phone, String code, String newPassword) {
        validateSmsCode(phone, code, Constants.SmsCodeType.RESET_PASSWORD);

        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getPhone, phone)
                        .eq(SysUser::getDeleted, 0)
        );
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        sysUserMapper.update(null,
                new LambdaUpdateWrapper<SysUser>()
                        .eq(SysUser::getId, user.getId())
                        .set(SysUser::getPassword, BCrypt.hashpw(newPassword))
        );

        smsCodeMapper.update(null,
                new LambdaUpdateWrapper<SmsCode>()
                        .eq(SmsCode::getPhone, phone)
                        .eq(SmsCode::getType, Constants.SmsCodeType.RESET_PASSWORD)
                        .eq(SmsCode::getCode, code)
                        .set(SmsCode::getUsed, 1)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAccount(Long userId, String reason) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        sysUserMapper.update(null,
                new LambdaUpdateWrapper<SysUser>()
                        .eq(SysUser::getId, userId)
                        .set(SysUser::getStatus, Constants.UserStatus.CANCEL)
        );

        redisUtil.delete(Constants.TOKEN_PREFIX + userId);
    }

    private void validateSmsCode(String phone, String code, Integer type) {
        SmsCode smsCode = smsCodeMapper.selectOne(
                new LambdaQueryWrapper<SmsCode>()
                        .eq(SmsCode::getPhone, phone)
                        .eq(SmsCode::getCode, code)
                        .eq(SmsCode::getType, type)
                        .eq(SmsCode::getUsed, 0)
                        .orderByDesc(SmsCode::getCreateTime)
                        .last("LIMIT 1")
        );

        if (smsCode == null) {
            throw new BusinessException("验证码错误");
        }

        if (smsCode.getExpireTime().before(new Date())) {
            throw new BusinessException("验证码已过期");
        }
    }

    private String generateUniqueInviteCode() {
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
}
