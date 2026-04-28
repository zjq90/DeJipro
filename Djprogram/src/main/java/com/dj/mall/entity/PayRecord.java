package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("pay_record")
public class PayRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String payNo;

    private Long orderId;

    private String orderNo;

    private Long userId;

    private Integer payType;

    private BigDecimal payAmount;

    private Integer payStatus;

    private String thirdPayNo;

    private Date payTime;

    private String failReason;

    private BigDecimal refundAmount;

    private Date refundTime;

    private String refundReason;

    private Date createTime;

    private Date updateTime;
}
