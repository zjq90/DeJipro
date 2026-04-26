package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long categoryId;

    private String goodsName;

    private String goodsSubtitle;

    private String mainImage;

    private String subImages;

    private String description;

    private String unit;

    private BigDecimal weight;

    private Integer isIntegralGoods;

    private BigDecimal sellPrice;

    private BigDecimal marketPrice;

    private BigDecimal costPrice;

    private Long integralPrice;

    private Integer stock;

    private Integer sellCount;

    private Integer viewCount;

    private Integer favoriteCount;

    private Integer commentCount;

    private BigDecimal goodCommentRate;

    private Integer isHot;

    private Integer isRecommend;

    private Integer isNew;

    private Integer status;

    private Integer sort;

    private Integer limitBuy;

    private Integer isFreeShipping;

    private Long shippingTemplateId;

    private BigDecimal commissionRate;

    private Long giveIntegral;

    private String tags;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
