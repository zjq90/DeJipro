package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("user_asset")
public class UserAsset implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private BigDecimal balance;

    private BigDecimal frozenBalance;

    private BigDecimal totalCommission;

    private BigDecimal availableCommission;

    private Long totalIntegral;

    private Long availableIntegral;

    private Long experience;

    private Date createTime;

    private Date updateTime;
}
