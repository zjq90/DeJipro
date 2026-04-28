package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("order_info")
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private String parentOrderNo;

    private Long userId;

    private Integer orderType;

    private Integer orderSource;

    private Integer orderStatus;

    private Integer payStatus;

    private BigDecimal goodsAmount;

    private BigDecimal freightAmount;

    private BigDecimal discountAmount;

    private BigDecimal couponAmount;

    private BigDecimal integralAmount;

    private BigDecimal payAmount;

    private Long usedIntegral;

    private Long giveIntegral;

    private Long couponId;

    private String receiverName;

    private String receiverPhone;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverFullAddress;

    private BigDecimal receiverLongitude;

    private BigDecimal receiverLatitude;

    private Integer pickupType;

    private Long pickupPointId;

    private String userRemark;

    private String merchantRemark;

    private Integer payType;

    private Date payTime;

    private String payOrderNo;

    private Date deliveryTime;

    private String logisticsCompany;

    private String logisticsNo;

    private Date receiveTime;

    private Date cancelTime;

    private String cancelReason;

    private Date finishTime;

    private Date autoConfirmTime;

    private Integer isComment;

    private Integer isDelete;

    private Long groupActivityId;

    private Long bargainActivityId;

    private Date createTime;

    private Date updateTime;
}
