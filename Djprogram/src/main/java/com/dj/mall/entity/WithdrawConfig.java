package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("withdraw_config")
public class WithdrawConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private BigDecimal dailyMaxAmount;

    private Integer dailyMaxTimes;

    private BigDecimal feeRate;

    private BigDecimal minFee;

    private BigDecimal maxFee;

    private Integer auditRequired;

    private Integer bankEnable;

    private Integer alipayEnable;

    private Integer wechatEnable;

    private String description;

    private Date createTime;

    private Date updateTime;
}
