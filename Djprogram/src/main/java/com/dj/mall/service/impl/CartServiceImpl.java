package com.dj.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.Cart;
import com.dj.mall.entity.Goods;
import com.dj.mall.entity.GoodsSku;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.CartMapper;
import com.dj.mall.mapper.GoodsMapper;
import com.dj.mall.mapper.GoodsSkuMapper;
import com.dj.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Override
    public List<Map<String, Object>> getCartList(Long userId) {
        List<Cart> carts = cartMapper.selectList(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .orderByDesc(Cart::getCreateTime)
        );

        List<Map<String, Object>> result = new ArrayList<>();
        for (Cart cart : carts) {
            Map<String, Object> item = buildCartItem(cart);
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public Integer getCartCount(Long userId) {
        return cartMapper.selectCount(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
        ).intValue();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCart(Long userId, Long goodsId, Long skuId, Integer quantity) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null || goods.getDeleted() == 1) {
            throw new BusinessException("商品不存在");
        }
        if (goods.getStatus() == Constants.GoodsStatus.OFF_SHELF) {
            throw new BusinessException("商品已下架");
        }

        GoodsSku sku = goodsSkuMapper.selectById(skuId);
        if (sku == null || !sku.getGoodsId().equals(goodsId)) {
            throw new BusinessException("商品规格不存在");
        }
        if (sku.getStatus() == Constants.Status.DISABLE) {
            throw new BusinessException("该规格已下架");
        }

        Cart existCart = cartMapper.selectOne(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .eq(Cart::getGoodsId, goodsId)
                        .eq(Cart::getSkuId, skuId)
        );

        if (existCart != null) {
            int newQuantity = existCart.getQuantity() + quantity;
            if (newQuantity > sku.getStock()) {
                throw new BusinessException("库存不足");
            }
            cartMapper.update(null,
                    new LambdaUpdateWrapper<Cart>()
                            .eq(Cart::getId, existCart.getId())
                            .set(Cart::getQuantity, newQuantity)
            );
        } else {
            if (quantity > sku.getStock()) {
                throw new BusinessException("库存不足");
            }
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setGoodsId(goodsId);
            cart.setSkuId(skuId);
            cart.setQuantity(quantity);
            cart.setSelected(Constants.DefaultFlag.YES);
            cartMapper.insert(cart);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCart(Long userId, Long cartId, Integer quantity) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null) {
            throw new BusinessException("购物车数据不存在");
        }
        if (!cart.getUserId().equals(userId)) {
            throw new BusinessException("无权操作");
        }

        GoodsSku sku = goodsSkuMapper.selectById(cart.getSkuId());
        if (sku != null && quantity > sku.getStock()) {
            throw new BusinessException("库存不足");
        }

        if (quantity <= 0) {
            cartMapper.deleteById(cartId);
        } else {
            cartMapper.update(null,
                    new LambdaUpdateWrapper<Cart>()
                            .eq(Cart::getId, cartId)
                            .set(Cart::getQuantity, quantity)
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSelected(Long userId, List<Long> cartIds, Integer selected) {
        if (cartIds == null || cartIds.isEmpty()) {
            return;
        }
        cartMapper.update(null,
                new LambdaUpdateWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .in(Cart::getId, cartIds)
                        .set(Cart::getSelected, selected)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectAll(Long userId, Integer selected) {
        cartMapper.update(null,
                new LambdaUpdateWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .set(Cart::getSelected, selected)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCart(Long userId, List<Long> cartIds) {
        if (cartIds == null || cartIds.isEmpty()) {
            return;
        }
        cartMapper.delete(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .in(Cart::getId, cartIds)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearCart(Long userId) {
        cartMapper.delete(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
        );
    }

    @Override
    public List<Map<String, Object>> getSelectedCartList(Long userId) {
        List<Cart> carts = cartMapper.selectList(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .eq(Cart::getSelected, Constants.DefaultFlag.YES)
                        .orderByDesc(Cart::getCreateTime)
        );

        List<Map<String, Object>> result = new ArrayList<>();
        for (Cart cart : carts) {
            Map<String, Object> item = buildCartItem(cart);
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }

    private Map<String, Object> buildCartItem(Cart cart) {
        Goods goods = goodsMapper.selectById(cart.getGoodsId());
        GoodsSku sku = goodsSkuMapper.selectById(cart.getSkuId());

        if (goods == null || sku == null) {
            return null;
        }

        Map<String, Object> item = new HashMap<>();
        item.put("cartId", cart.getId());
        item.put("goodsId", cart.getGoodsId());
        item.put("skuId", cart.getSkuId());
        item.put("quantity", cart.getQuantity());
        item.put("selected", cart.getSelected());

        item.put("goodsName", goods.getGoodsName());
        item.put("mainImage", goods.getMainImage());
        item.put("goodsStatus", goods.getStatus());

        item.put("skuName", sku.getSkuName());
        item.put("specs", sku.getSpecs());
        item.put("sellPrice", sku.getSellPrice());
        item.put("integralPrice", sku.getIntegralPrice());
        item.put("skuStock", sku.getStock());
        item.put("skuStatus", sku.getStatus());

        return item;
    }
}
