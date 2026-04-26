package com.dj.mall.service;

import com.dj.mall.entity.Cart;

import java.util.List;
import java.util.Map;

public interface CartService {

    List<Map<String, Object>> getCartList(Long userId);

    Integer getCartCount(Long userId);

    void addCart(Long userId, Long goodsId, Long skuId, Integer quantity);

    void updateCart(Long userId, Long cartId, Integer quantity);

    void updateSelected(Long userId, List<Long> cartIds, Integer selected);

    void selectAll(Long userId, Integer selected);

    void deleteCart(Long userId, List<Long> cartIds);

    void clearCart(Long userId);

    List<Map<String, Object>> getSelectedCartList(Long userId);
}
