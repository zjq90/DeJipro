package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("goods_sku")
public class GoodsSku implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long goodsId;

    private String skuName;

    private String specs;

    private String specValueIds;

    private BigDecimal sellPrice;

    private BigDecimal costPrice;

    private Long integralPrice;

    private Integer stock;

    private String image;

    private BigDecimal weight;

    private String code;

    private String barcode;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
