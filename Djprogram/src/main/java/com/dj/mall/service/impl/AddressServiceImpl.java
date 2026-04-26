package com.dj.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.UserAddress;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.UserAddressMapper;
import com.dj.mall.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> getAddressList(Long userId) {
        return userAddressMapper.selectList(
                new LambdaQueryWrapper<UserAddress>()
                        .eq(UserAddress::getUserId, userId)
                        .eq(UserAddress::getDeleted, 0)
                        .orderByDesc(UserAddress::getIsDefault)
                        .orderByDesc(UserAddress::getCreateTime)
        );
    }

    @Override
    public UserAddress getAddressDetail(Long userId, Long addressId) {
        UserAddress address = userAddressMapper.selectById(addressId);
        if (address == null || address.getDeleted() == 1) {
            throw new BusinessException("地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            throw new BusinessException("无权访问");
        }
        return address;
    }

    @Override
    public UserAddress getDefaultAddress(Long userId) {
        return userAddressMapper.selectOne(
                new LambdaQueryWrapper<UserAddress>()
                        .eq(UserAddress::getUserId, userId)
                        .eq(UserAddress::getIsDefault, Constants.DefaultFlag.YES)
                        .eq(UserAddress::getDeleted, 0)
                        .last("LIMIT 1")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAddress(Long userId, UserAddress address) {
        address.setUserId(userId);
        
        if (address.getFullAddress() == null || address.getFullAddress().isEmpty()) {
            StringBuilder fullAddress = new StringBuilder();
            if (address.getProvince() != null) {
                fullAddress.append(address.getProvince());
            }
            if (address.getCity() != null) {
                fullAddress.append(address.getCity());
            }
            if (address.getDistrict() != null) {
                fullAddress.append(address.getDistrict());
            }
            if (address.getAddress() != null) {
                fullAddress.append(address.getAddress());
            }
            address.setFullAddress(fullAddress.toString());
        }

        Long count = userAddressMapper.selectCount(
                new LambdaQueryWrapper<UserAddress>()
                        .eq(UserAddress::getUserId, userId)
                        .eq(UserAddress::getDeleted, 0)
        );

        if (count == 0 || address.getIsDefault() != null && address.getIsDefault() == 1) {
            userAddressMapper.update(null,
                    new LambdaUpdateWrapper<UserAddress>()
                            .eq(UserAddress::getUserId, userId)
                            .set(UserAddress::getIsDefault, Constants.DefaultFlag.NO)
            );
            address.setIsDefault(Constants.DefaultFlag.YES);
        } else {
            address.setIsDefault(Constants.DefaultFlag.NO);
        }

        userAddressMapper.insert(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(Long userId, UserAddress address) {
        UserAddress existAddress = userAddressMapper.selectById(address.getId());
        if (existAddress == null || existAddress.getDeleted() == 1) {
            throw new BusinessException("地址不存在");
        }
        if (!existAddress.getUserId().equals(userId)) {
            throw new BusinessException("无权修改");
        }

        if (address.getFullAddress() == null || address.getFullAddress().isEmpty()) {
            StringBuilder fullAddress = new StringBuilder();
            if (address.getProvince() != null) {
                fullAddress.append(address.getProvince());
            }
            if (address.getCity() != null) {
                fullAddress.append(address.getCity());
            }
            if (address.getDistrict() != null) {
                fullAddress.append(address.getDistrict());
            }
            if (address.getAddress() != null) {
                fullAddress.append(address.getAddress());
            }
            address.setFullAddress(fullAddress.toString());
        }

        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            userAddressMapper.update(null,
                    new LambdaUpdateWrapper<UserAddress>()
                            .eq(UserAddress::getUserId, userId)
                            .set(UserAddress::getIsDefault, Constants.DefaultFlag.NO)
            );
        }

        address.setUserId(null);
        userAddressMapper.updateById(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(Long userId, Long addressId) {
        UserAddress address = userAddressMapper.selectById(addressId);
        if (address == null || address.getDeleted() == 1) {
            throw new BusinessException("地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            throw new BusinessException("无权删除");
        }

        userAddressMapper.update(null,
                new LambdaUpdateWrapper<UserAddress>()
                        .eq(UserAddress::getId, addressId)
                        .set(UserAddress::getDeleted, 1)
        );

        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            UserAddress firstAddress = userAddressMapper.selectOne(
                    new LambdaQueryWrapper<UserAddress>()
                            .eq(UserAddress::getUserId, userId)
                            .eq(UserAddress::getDeleted, 0)
                            .orderByDesc(UserAddress::getCreateTime)
                            .last("LIMIT 1")
            );
            if (firstAddress != null) {
                userAddressMapper.update(null,
                        new LambdaUpdateWrapper<UserAddress>()
                                .eq(UserAddress::getId, firstAddress.getId())
                                .set(UserAddress::getIsDefault, Constants.DefaultFlag.YES)
                );
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(Long userId, Long addressId) {
        UserAddress address = userAddressMapper.selectById(addressId);
        if (address == null || address.getDeleted() == 1) {
            throw new BusinessException("地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            throw new BusinessException("无权操作");
        }

        userAddressMapper.update(null,
                new LambdaUpdateWrapper<UserAddress>()
                        .eq(UserAddress::getUserId, userId)
                        .set(UserAddress::getIsDefault, Constants.DefaultFlag.NO)
        );

        userAddressMapper.update(null,
                new LambdaUpdateWrapper<UserAddress>()
                        .eq(UserAddress::getId, addressId)
                        .set(UserAddress::getIsDefault, Constants.DefaultFlag.YES)
        );
    }
}
