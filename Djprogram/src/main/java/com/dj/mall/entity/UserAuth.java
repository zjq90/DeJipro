package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user_auth")
public class UserAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String realName;

    private String idCard;

    private String idCardFront;

    private String idCardBack;

    private String idCardHand;

    private Integer status;

    private String rejectReason;

    private Date auditTime;

    private Long auditAdminId;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
