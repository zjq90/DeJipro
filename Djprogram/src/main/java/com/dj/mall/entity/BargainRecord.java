package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bargain_record")
public class BargainRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long bargainActivityId;

    private Long helpUserId;

    private String helpUserNickname;

    private String helpUserAvatar;

    private BigDecimal bargainAmount;

    private Integer isNewUser;

    private Date bargainTime;

    private Date createTime;
}
