package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("promotion_poster")
public class PromotionPoster implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String posterName;

    private Integer posterType;

    private String backgroundImage;

    private Integer qrcodeX;

    private Integer qrcodeY;

    private Integer qrcodeWidth;

    private Integer qrcodeHeight;

    private Integer avatarX;

    private Integer avatarY;

    private Integer avatarWidth;

    private Integer avatarHeight;

    private Integer nicknameX;

    private Integer nicknameY;

    private Integer nicknameFontSize;

    private String nicknameColor;

    private String description;

    private Integer sort;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
