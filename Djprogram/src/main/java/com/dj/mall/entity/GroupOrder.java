package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("group_order")
public class GroupOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long groupActivityId;

    private Long userId;

    private Long orderId;

    private String orderNo;

    private Integer isLeader;

    private Date joinTime;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
