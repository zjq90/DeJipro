package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("withdraw_record")
public class WithdrawRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String withdrawNo;

    private BigDecimal amount;

    private BigDecimal fee;

    private BigDecimal actualAmount;

    private Integer withdrawType;

    private Long bankId;

    private String bankName;

    private String branchName;

    private String accountName;

    private String accountNo;

    private String alipayName;

    private String alipayAccount;

    private String wechatName;

    private String wechatOpenid;

    private Integer status;

    private String rejectReason;

    private Date auditTime;

    private Long auditAdminId;

    private Date transferTime;

    private String transferNo;

    private String remark;

    private Date createTime;

    private Date updateTime;
}
