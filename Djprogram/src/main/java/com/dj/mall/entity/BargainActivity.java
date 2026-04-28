package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bargain_activity")
public class BargainActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bargainGoodsId;

    private Long userId;

    private BigDecimal currentPrice;

    private BigDecimal bargainAmount;

    private Integer bargainCount;

    private Integer status;

    private Date startTime;

    private Date endTime;

    private Long orderId;

    private Date createTime;

    private Date updateTime;
}
