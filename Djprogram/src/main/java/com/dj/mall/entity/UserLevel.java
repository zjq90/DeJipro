package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("user_level")
public class UserLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String levelName;

    private Integer levelValue;

    private Long minExperience;

    private Long maxExperience;

    private BigDecimal discount;

    private String icon;

    private String description;

    private String privileges;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
