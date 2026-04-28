package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user_subscribe")
public class UserSubscribe implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long templateId;

    private String templateCode;

    private Integer subscribeType;

    private Date subscribeTime;

    private Date expireTime;

    private Integer usedCount;

    private Integer maxCount;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
