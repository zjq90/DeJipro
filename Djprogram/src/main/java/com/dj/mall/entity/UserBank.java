package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("user_bank")
public class UserBank implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String bankName;

    private String bankCode;

    private String branchName;

    private String accountName;

    private String accountNo;

    private Integer isDefault;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
