package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("coupon")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String couponName;

    private Integer couponType;

    private Integer discountType;

    private BigDecimal discountAmount;

    private BigDecimal minAmount;

    private BigDecimal maxDiscountAmount;

    private Integer totalCount;

    private Integer receiveCount;

    private Integer useCount;

    private Integer perLimit;

    private Date receiveStartTime;

    private Date receiveEndTime;

    private Date validStartTime;

    private Date validEndTime;

    private Integer validDays;

    private Integer applyType;

    private String applyValue;

    private String excludeValue;

    private String description;

    private Integer sort;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
