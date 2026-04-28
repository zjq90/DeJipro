package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long categoryId;

    private String articleTitle;

    private String articleSubtitle;

    private String coverImage;

    private String author;

    private String source;

    private String summary;

    private String content;

    private Integer viewCount;

    private Integer likeCount;

    private Integer favoriteCount;

    private Integer commentCount;

    private Integer shareCount;

    private Integer isTop;

    private Integer isHot;

    private Integer isRecommend;

    private Integer allowComment;

    private Integer sort;

    private Integer status;

    private Date publishTime;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
