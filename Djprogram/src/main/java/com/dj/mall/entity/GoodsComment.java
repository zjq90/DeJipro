package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("goods_comment")
public class GoodsComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long orderItemId;

    private Long goodsId;

    private Long skuId;

    private Long userId;

    private String orderNo;

    private String content;

    private String images;

    private String video;

    private Integer star;

    private Integer isAnonymous;

    private Integer isTop;

    private Integer isHot;

    private String reply;

    private Date replyTime;

    private Integer likeCount;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
