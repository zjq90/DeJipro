package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("group_goods")
public class GroupGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long goodsId;

    private Long skuId;

    private String activityName;

    private Integer groupType;

    private Integer minPeople;

    private Integer maxPeople;

    private BigDecimal groupPrice;

    private BigDecimal originalPrice;

    private Integer totalStock;

    private Integer usedStock;

    private Integer limitBuy;

    private Date startTime;

    private Date endTime;

    private Integer validHours;

    private String description;

    private Integer sort;

    private Integer isRecommend;

    private Integer isHot;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
