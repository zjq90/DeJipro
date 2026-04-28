package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("banner")
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String bannerName;

    private Integer bannerType;

    private String imageUrl;

    private Integer linkType;

    private String linkValue;

    private Integer sort;

    private Integer status;

    private Date startTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
