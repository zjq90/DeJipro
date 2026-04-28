package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("subscribe_template")
public class SubscribeTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String templateName;

    private String templateCode;

    private Integer templateType;

    private String scene;

    private String title;

    private String content;

    private String example;

    private String pagePath;

    private Integer sort;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
