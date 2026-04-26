package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("commission_record")
public class CommissionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long orderId;

    private String orderNo;

    private Long fromUserId;

    private Integer level;

    private BigDecimal amount;

    private BigDecimal rate;

    private String description;

    private Integer status;

    private Date settleTime;

    private Date createTime;
}
