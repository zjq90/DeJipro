package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("recharge_amount")
public class RechargeAmount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private BigDecimal amount;

    private BigDecimal giveAmount;

    private Long giveIntegral;

    private Integer sort;

    private Integer status;

    private String description;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
