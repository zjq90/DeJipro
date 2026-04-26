package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("goods_category")
public class GoodsCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parentId;

    private String categoryName;

    private String icon;

    private String banner;

    private Integer sort;

    private Integer level;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
