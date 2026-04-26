package com.dj.mall.service;

import com.dj.mall.entity.UserAddress;

import java.util.List;

public interface AddressService {

    List<UserAddress> getAddressList(Long userId);

    UserAddress getAddressDetail(Long userId, Long addressId);

    UserAddress getDefaultAddress(Long userId);

    void addAddress(Long userId, UserAddress address);

    void updateAddress(Long userId, UserAddress address);

    void deleteAddress(Long userId, Long addressId);

    void setDefaultAddress(Long userId, Long addressId);
}
