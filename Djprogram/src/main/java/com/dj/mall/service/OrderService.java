package com.dj.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dj.mall.entity.OrderInfo;
import com.dj.mall.entity.OrderItem;
import com.dj.mall.entity.PayRecord;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderInfo createOrder(Long userId, Map<String, Object> orderData);

    Page<OrderInfo> getOrderPage(Long userId, Integer pageNum, Integer pageSize, Integer orderStatus, Integer orderType);

    OrderInfo getOrderById(Long id);

    OrderInfo getOrderByNo(String orderNo);

    List<OrderItem> getOrderItems(Long orderId);

    void cancelOrder(Long orderId, String reason);

    void confirmReceive(Long orderId);

    PayRecord createPayRecord(Long orderId, Integer payType);

    PayRecord getPayRecordByOrderId(Long orderId);

    PayRecord getPayRecordByNo(String payNo);

    void updatePayStatus(String payNo, Integer payStatus, String thirdPayNo);

    Map<String, Object> queryPayResult(String orderNo);
}
