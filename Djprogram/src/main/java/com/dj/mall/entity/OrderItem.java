package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("order_item")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private String orderNo;

    private Long userId;

    private Long goodsId;

    private Long skuId;

    private String goodsName;

    private String goodsImage;

    private String skuName;

    private String skuSpecs;

    private String unit;

    private Integer quantity;

    private BigDecimal originalPrice;

    private BigDecimal sellPrice;

    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    private Long giveIntegral;

    private Integer isComment;

    private Long commentId;

    private Integer afterSaleStatus;

    private Date createTime;

    private Date updateTime;
}
