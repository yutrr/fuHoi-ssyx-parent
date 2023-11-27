package com.xie.ssyx.product.controller;

import com.xie.ssyx.common.result.Result;
import com.xie.ssyx.product.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "文件上传接口")
@RestController
@RequestMapping("admin/product")
//@CrossOrigin
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    //图片上传的方法OSS方法
    @ApiOperation("图片上传OSS方法")
    @PostMapping("fileUploads")
    public Result fileUpload(MultipartFile file) {
        String url = fileUploadService.uploadFile(file);
        return Result.ok(url);
    }


    //图片上传方法Minio
    @ApiOperation("图片上传Minio方法")
    @PostMapping("fileUpload")
    public Result fileUploadMinio(MultipartFile file){
        String url = fileUploadService.uploadFileMinio(file);
        return Result.ok(url);
    }

}
