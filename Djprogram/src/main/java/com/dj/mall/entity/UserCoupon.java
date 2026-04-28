package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("user_coupon")
public class UserCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long couponId;

    private String couponName;

    private Integer couponType;

    private Integer discountType;

    private BigDecimal discountAmount;

    private BigDecimal minAmount;

    private Long orderId;

    private Integer status;

    private Date receiveTime;

    private Date useTime;

    private Date validStartTime;

    private Date validEndTime;

    private Date createTime;

    private Date updateTime;
}
