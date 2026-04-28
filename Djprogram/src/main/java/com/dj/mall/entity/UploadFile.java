package com.dj.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("upload_file")
public class UploadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String fileName;

    private String filePath;

    private String fileUrl;

    private Long fileSize;

    private String fileType;

    private String fileExtension;

    private String moduleType;

    private Long userId;

    private Date createTime;
}
