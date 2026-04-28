package com.dj.mall.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.common.Constants;
import com.dj.mall.entity.*;
import com.dj.mall.exception.BusinessException;
import com.dj.mall.mapper.*;
import com.dj.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PayRecordMapper payRecordMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private UserAssetMapper userAssetMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderInfo createOrder(Long userId, Map<String, Object> orderData) {
        String orderNo = generateOrderNo();
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) orderData.get("items");
        
        BigDecimal totalGoodsAmount = BigDecimal.ZERO;
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;
        long totalGiveIntegral = 0L;
        
        for (Map<String, Object> item : items) {
            Long goodsId = ((Number) item.get("goodsId")).longValue();
            Long skuId = ((Number) item.get("skuId")).longValue();
            int quantity = ((Number) item.get("quantity")).intValue();
            
            Goods goods = goodsMapper.selectById(goodsId);
            GoodsSku sku = goodsSkuMapper.selectById(skuId);
            
            if (goods == null || goods.getDeleted() == 1) {
                throw new BusinessException("商品不存在");
            }
            if (!Constants.GoodsStatus.ON_SHELF.equals(goods.getStatus())) {
                throw new BusinessException("商品已下架");
            }
            if (sku == null) {
                throw new BusinessException("商品规格不存在");
            }
            if (sku.getStock() < quantity) {
                throw new BusinessException("商品库存不足");
            }
            
            BigDecimal itemAmount = sku.getSellPrice().multiply(BigDecimal.valueOf(quantity));
            totalGoodsAmount = totalGoodsAmount.add(itemAmount);
            totalGiveIntegral += goods.getGiveIntegral() * quantity;
        }
        
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(orderNo);
        orderInfo.setUserId(userId);
        orderInfo.setOrderType((Integer) orderData.getOrDefault("orderType", Constants.OrderType.NORMAL));
        orderInfo.setOrderSource((Integer) orderData.getOrDefault("orderSource", Constants.OrderSource.MINI_PROGRAM));
        orderInfo.setOrderStatus(Constants.OrderStatus.PENDING_PAYMENT);
        orderInfo.setPayStatus(Constants.PayStatus.UNPAID);
        orderInfo.setReceiverName((String) orderData.get("receiverName"));
        orderInfo.setReceiverPhone((String) orderData.get("receiverPhone"));
        orderInfo.setReceiverProvince((String) orderData.get("receiverProvince"));
        orderInfo.setReceiverCity((String) orderData.get("receiverCity"));
        orderInfo.setReceiverDistrict((String) orderData.get("receiverDistrict"));
        orderInfo.setReceiverAddress((String) orderData.get("receiverAddress"));
        orderInfo.setReceiverFullAddress((String) orderData.get("receiverFullAddress"));
        orderInfo.setPickupType((Integer) orderData.getOrDefault("pickupType", Constants.PickupType.DELIVERY));
        orderInfo.setUserRemark((String) orderData.get("userRemark"));
        orderInfo.setCouponId((Long) orderData.get("couponId"));
        orderInfo.setUsedIntegral((Long) orderData.getOrDefault("usedIntegral", 0L));
        orderInfo.setGroupActivityId((Long) orderData.get("groupActivityId"));
        orderInfo.setBargainActivityId((Long) orderData.get("bargainActivityId"));
        orderInfo.setGoodsAmount(totalGoodsAmount);
        orderInfo.setFreightAmount(BigDecimal.ZERO);
        orderInfo.setDiscountAmount(totalDiscountAmount);
        orderInfo.setCouponAmount(BigDecimal.ZERO);
        orderInfo.setIntegralAmount(BigDecimal.ZERO);
        orderInfo.setPayAmount(totalGoodsAmount.subtract(totalDiscountAmount));
        orderInfo.setGiveIntegral(totalGiveIntegral);
        orderInfo.setIsComment(Constants.DefaultFlag.NO);
        orderInfo.setIsDelete(Constants.DefaultFlag.NO);
        
        orderInfoMapper.insert(orderInfo);
        
        for (Map<String, Object> item : items) {
            Long goodsId = ((Number) item.get("goodsId")).longValue();
            Long skuId = ((Number) item.get("skuId")).longValue();
            int quantity = ((Number) item.get("quantity")).intValue();
            
            Goods goods = goodsMapper.selectById(goodsId);
            GoodsSku sku = goodsSkuMapper.selectById(skuId);
            
            BigDecimal itemAmount = sku.getSellPrice().multiply(BigDecimal.valueOf(quantity));
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderInfo.getId());
            orderItem.setOrderNo(orderNo);
            orderItem.setUserId(userId);
            orderItem.setGoodsId(goodsId);
            orderItem.setSkuId(skuId);
            orderItem.setGoodsName(goods.getGoodsName());
            orderItem.setGoodsImage(goods.getMainImage());
            orderItem.setSkuName(sku.getSkuName());
            orderItem.setSkuSpecs(sku.getSpecs());
            orderItem.setUnit(goods.getUnit());
            orderItem.setQuantity(quantity);
            orderItem.setOriginalPrice(sku.getSellPrice());
            orderItem.setSellPrice(sku.getSellPrice());
            orderItem.setTotalAmount(itemAmount);
            orderItem.setGiveIntegral(goods.getGiveIntegral() * quantity);
            orderItem.setIsComment(Constants.DefaultFlag.NO);
            orderItem.setAfterSaleStatus(Constants.DefaultFlag.NO);
            
            orderItemMapper.insert(orderItem);
            
            goodsSkuMapper.update(null,
                    new LambdaUpdateWrapper<GoodsSku>()
                            .eq(GoodsSku::getId, skuId)
                            .setSql("stock = stock - " + quantity)
            );
        }
        
        return orderInfo;
    }

    private String generateOrderNo() {
        String datePart = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        String randomPart = IdUtil.getSnowflake(1, 1).nextIdStr().substring(6, 12);
        return "DJ" + datePart + randomPart;
    }

    @Override
    public Page<OrderInfo> getOrderPage(Long userId, Integer pageNum, Integer pageSize, Integer orderStatus, Integer orderType) {
        Page<OrderInfo> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(OrderInfo::getUserId, userId)
                .eq(OrderInfo::getIsDelete, Constants.DefaultFlag.NO);
        
        if (orderStatus != null) {
            wrapper.eq(OrderInfo::getOrderStatus, orderStatus);
        }
        
        if (orderType != null) {
            wrapper.eq(OrderInfo::getOrderType, orderType);
        }
        
        wrapper.orderByDesc(OrderInfo::getCreateTime);
        
        return orderInfoMapper.selectPage(page, wrapper);
    }

    @Override
    public OrderInfo getOrderById(Long id) {
        OrderInfo orderInfo = orderInfoMapper.selectById(id);
        if (orderInfo == null) {
            throw new BusinessException("订单不存在");
        }
        return orderInfo;
    }

    @Override
    public OrderInfo getOrderByNo(String orderNo) {
        OrderInfo orderInfo = orderInfoMapper.selectOne(
                new LambdaQueryWrapper<OrderInfo>()
                        .eq(OrderInfo::getOrderNo, orderNo)
        );
        if (orderInfo == null) {
            throw new BusinessException("订单不存在");
        }
        return orderInfo;
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>()
                        .eq(OrderItem::getOrderId, orderId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, String reason) {
        OrderInfo orderInfo = getOrderById(orderId);
        
        if (!Constants.OrderStatus.PENDING_PAYMENT.equals(orderInfo.getOrderStatus())) {
            throw new BusinessException("该订单无法取消");
        }
        
        orderInfoMapper.update(null,
                new LambdaUpdateWrapper<OrderInfo>()
                        .eq(OrderInfo::getId, orderId)
                        .set(OrderInfo::getOrderStatus, Constants.OrderStatus.CANCELLED)
                        .set(OrderInfo::getCancelTime, new Date())
                        .set(OrderInfo::getCancelReason, reason)
        );
        
        List<OrderItem> items = getOrderItems(orderId);
        for (OrderItem item : items) {
            goodsSkuMapper.update(null,
                    new LambdaUpdateWrapper<GoodsSku>()
                            .eq(GoodsSku::getId, item.getSkuId())
                            .setSql("stock = stock + " + item.getQuantity())
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmReceive(Long orderId) {
        OrderInfo orderInfo = getOrderById(orderId);
        
        if (!Constants.OrderStatus.PENDING_RECEIVE.equals(orderInfo.getOrderStatus())) {
            throw new BusinessException("该订单无法确认收货");
        }
        
        orderInfoMapper.update(null,
                new LambdaUpdateWrapper<OrderInfo>()
                        .eq(OrderInfo::getId, orderId)
                        .set(OrderInfo::getOrderStatus, Constants.OrderStatus.COMPLETED)
                        .set(OrderInfo::getReceiveTime, new Date())
                        .set(OrderInfo::getFinishTime, new Date())
        );
        
        if (orderInfo.getGiveIntegral() != null && orderInfo.getGiveIntegral() > 0) {
            userAssetMapper.update(null,
                    new LambdaUpdateWrapper<UserAsset>()
                            .eq(UserAsset::getUserId, orderInfo.getUserId())
                            .setSql("available_integral = available_integral + " + orderInfo.getGiveIntegral())
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayRecord createPayRecord(Long orderId, Integer payType) {
        OrderInfo orderInfo = getOrderById(orderId);
        
        if (!Constants.OrderStatus.PENDING_PAYMENT.equals(orderInfo.getOrderStatus())) {
            throw new BusinessException("该订单无法支付");
        }
        
        String payNo = "PAY" + System.currentTimeMillis() + IdUtil.getSnowflake(1, 1).nextIdStr().substring(6, 10);
        
        PayRecord payRecord = new PayRecord();
        payRecord.setPayNo(payNo);
        payRecord.setOrderId(orderId);
        payRecord.setOrderNo(orderInfo.getOrderNo());
        payRecord.setUserId(orderInfo.getUserId());
        payRecord.setPayType(payType);
        payRecord.setPayAmount(orderInfo.getPayAmount());
        payRecord.setPayStatus(Constants.PayStatus.UNPAID);
        
        payRecordMapper.insert(payRecord);
        
        return payRecord;
    }

    @Override
    public PayRecord getPayRecordByOrderId(Long orderId) {
        return payRecordMapper.selectOne(
                new LambdaQueryWrapper<PayRecord>()
                        .eq(PayRecord::getOrderId, orderId)
                        .orderByDesc(PayRecord::getCreateTime)
                        .last("LIMIT 1")
        );
    }

    @Override
    public PayRecord getPayRecordByNo(String payNo) {
        PayRecord payRecord = payRecordMapper.selectOne(
                new LambdaQueryWrapper<PayRecord>()
                        .eq(PayRecord::getPayNo, payNo)
        );
        if (payRecord == null) {
            throw new BusinessException("支付记录不存在");
        }
        return payRecord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePayStatus(String payNo, Integer payStatus, String thirdPayNo) {
        PayRecord payRecord = getPayRecordByNo(payNo);
        
        if (Constants.PayStatus.PAID.equals(payRecord.getPayStatus())) {
            return;
        }
        
        payRecordMapper.update(null,
                new LambdaUpdateWrapper<PayRecord>()
                        .eq(PayRecord::getId, payRecord.getId())
                        .set(PayRecord::getPayStatus, payStatus)
                        .set(PayRecord::getThirdPayNo, thirdPayNo)
                        .set(PayRecord::getPayTime, new Date())
        );
        
        if (Constants.PayStatus.PAID.equals(payStatus)) {
            orderInfoMapper.update(null,
                    new LambdaUpdateWrapper<OrderInfo>()
                            .eq(OrderInfo::getId, payRecord.getOrderId())
                            .set(OrderInfo::getOrderStatus, Constants.OrderStatus.PENDING_SHIPMENT)
                            .set(OrderInfo::getPayStatus, Constants.PayStatus.PAID)
                            .set(OrderInfo::getPayType, payRecord.getPayType())
                            .set(OrderInfo::getPayTime, new Date())
                            .set(OrderInfo::getPayOrderNo, thirdPayNo)
            );
        }
    }

    @Override
    public Map<String, Object> queryPayResult(String orderNo) {
        OrderInfo orderInfo = getOrderByNo(orderNo);
        
        Map<String, Object> result = new HashMap<>();
        result.put("orderNo", orderInfo.getOrderNo());
        result.put("orderStatus", orderInfo.getOrderStatus());
        result.put("payStatus", orderInfo.getPayStatus());
        result.put("payAmount", orderInfo.getPayAmount());
        
        if (Constants.PayStatus.PAID.equals(orderInfo.getPayStatus())) {
            result.put("payTime", orderInfo.getPayTime());
        }
        
        return result;
    }
}
