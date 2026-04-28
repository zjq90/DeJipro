package com.dj.mall.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.dj.mall.common.Result;
import com.dj.mall.entity.UploadFile;
import com.dj.mall.mapper.UploadFileMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${file.upload.path:/data/upload/}")
    private String uploadPath;

    @Value("${file.upload.url:http://localhost:8080/api/upload/}")
    private String uploadUrl;

    @Autowired
    private UploadFileMapper uploadFileMapper;

    @ApiOperation("上传单张图片")
    @PostMapping("/image")
    public Result<Map<String, Object>> uploadImage(
            @ApiParam(value = "图片文件") @RequestParam("file") MultipartFile file,
            @ApiParam(value = "模块类型") @RequestParam(required = false) String moduleType,
            @ApiParam(value = "用户ID") @RequestParam(required = false) Long userId) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        
        String originalFilename = file.getOriginalFilename();
        String extension = FileUtil.extName(originalFilename);
        
        List<String> allowedExtensions = new ArrayList<>();
        allowedExtensions.add("jpg");
        allowedExtensions.add("jpeg");
        allowedExtensions.add("png");
        allowedExtensions.add("gif");
        allowedExtensions.add("bmp");
        allowedExtensions.add("webp");
        
        if (extension == null || !allowedExtensions.contains(extension.toLowerCase())) {
            return Result.error("不支持的图片格式");
        }
        
        try {
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String savePath = uploadPath + datePath + "/";
            String newFileName = IdUtil.getSnowflake(1, 1).nextIdStr() + "." + extension.toLowerCase();
            
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            
            File destFile = new File(savePath + newFileName);
            file.transferTo(destFile);
            
            String fileUrl = uploadUrl + datePath + "/" + newFileName;
            
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFileName(originalFilename);
            uploadFile.setFilePath(savePath + newFileName);
            uploadFile.setFileUrl(fileUrl);
            uploadFile.setFileSize(file.getSize());
            uploadFile.setFileType(file.getContentType());
            uploadFile.setFileExtension(extension.toLowerCase());
            uploadFile.setModuleType(moduleType);
            uploadFile.setUserId(userId);
            
            uploadFileMapper.insert(uploadFile);
            
            Map<String, Object> result = new HashMap<>();
            result.put("fileName", originalFilename);
            result.put("fileUrl", fileUrl);
            result.put("fileSize", file.getSize());
            result.put("fileType", file.getContentType());
            
            return Result.success(result);
            
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量上传图片")
    @PostMapping("/images")
    public Result<List<Map<String, Object>>> uploadImages(
            @ApiParam(value = "图片文件列表") @RequestParam("files") MultipartFile[] files,
            @ApiParam(value = "模块类型") @RequestParam(required = false) String moduleType,
            @ApiParam(value = "用户ID") @RequestParam(required = false) Long userId) {
        if (files == null || files.length == 0) {
            return Result.error("文件不能为空");
        }
        
        List<Map<String, Object>> resultList = new ArrayList<>();
        
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                Result<Map<String, Object>> result = uploadImage(file, moduleType, userId);
                if (result.getCode() == 200 && result.getData() != null) {
                    resultList.add(result.getData());
                }
            }
        }
        
        return Result.success(resultList);
    }

    @ApiOperation("获取文件列表")
    @GetMapping("/list")
    public Result<List<UploadFile>> getFileList(
            @ApiParam(value = "用户ID") @RequestParam(required = false) Long userId,
            @ApiParam(value = "模块类型") @RequestParam(required = false) String moduleType) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UploadFile> wrapper = 
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        
        if (userId != null) {
            wrapper.eq(UploadFile::getUserId, userId);
        }
        
        if (moduleType != null && !moduleType.isEmpty()) {
            wrapper.eq(UploadFile::getModuleType, moduleType);
        }
        
        wrapper.orderByDesc(UploadFile::getCreateTime);
        
        List<UploadFile> list = uploadFileMapper.selectList(wrapper);
        return Result.success(list);
    }
}
