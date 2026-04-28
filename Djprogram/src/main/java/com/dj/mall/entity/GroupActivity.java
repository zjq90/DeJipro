package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("group_activity")
public class GroupActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long groupGoodsId;

    private Long leaderId;

    private Long leaderOrderId;

    private Integer currentPeople;

    private Integer needPeople;

    private Integer status;

    private Date startTime;

    private Date endTime;

    private Date successTime;

    private String failReason;

    private Date createTime;

    private Date updateTime;
}
