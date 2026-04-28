package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("pickup_point")
public class PickupPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String pointName;

    private String pointCode;

    private String contactPerson;

    private String contactPhone;

    private String province;

    private String city;

    private String district;

    private String address;

    private String fullAddress;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String businessHours;

    private String images;

    private String description;

    private Integer sort;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
