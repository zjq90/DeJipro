package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String phone;

    private String email;

    private String nickname;

    private String avatar;

    private Integer gender;

    private Date birthday;

    private Integer status;

    private Date lastLoginTime;

    private String lastLoginIp;

    private String inviteCode;

    private Long parentId;

    private Long levelId;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;
}
