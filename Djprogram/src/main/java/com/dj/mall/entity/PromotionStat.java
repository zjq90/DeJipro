package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("promotion_stat")
public class PromotionStat implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Date statDate;

    private Integer pvCount;

    private Integer uvCount;

    private Integer registerCount;

    private Integer orderCount;

    private BigDecimal orderAmount;

    private BigDecimal commissionAmount;

    private Date createTime;

    private Date updateTime;
}
