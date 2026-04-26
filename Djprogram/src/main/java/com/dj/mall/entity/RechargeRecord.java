package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("recharge_record")
public class RechargeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String rechargeNo;

    private String orderNo;

    private BigDecimal amount;

    private BigDecimal giveAmount;

    private Long giveIntegral;

    private Integer payType;

    private Integer status;

    private Date payTime;

    private Integer platform;

    private String remark;

    private Date createTime;

    private Date updateTime;
}
