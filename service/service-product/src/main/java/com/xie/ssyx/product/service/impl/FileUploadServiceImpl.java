package com.xie.ssyx.product.service.impl;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.xie.ssyx.common.result.Result;
import com.xie.ssyx.common.result.ResultCodeEnum;
import com.xie.ssyx.common.service.FileStorageService;
import com.xie.ssyx.product.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${aliyun.endpoint}")
    private String endpoint;

    @Value("${aliyun.keyid}")
    private String accessKeyId;

    @Value("${aliyun.keysecret}")
    private String accessKeySecret;

    @Value("${aliyun.bucketname}")
    private String bucketName;

    @Autowired
    private FileStorageService fileStorageService;

    //图片上传的方法
    @Override
    public String uploadFile(MultipartFile file) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKeyId, accessKeySecret);

        try {
            //上传文件输入流
            InputStream inputStream = file.getInputStream();

            //获取文件实际名称
            String objectName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            objectName = uuid+objectName;

            //对上传文件进行分组，根据当前年/月/日
            // objectName:  2023/10/10/uuid01.jpg
            String currentDateTime = new DateTime().toString("yyyy/MM/dd");
            objectName = currentDateTime+"/"+objectName;

            // 创建PutObjectRequest对象
            //第一个 bucket名称
            //第二个 上传文件路径+名称
            /// 比如只有名称 01.jpg
            /// 比如 /aa/bb/001.jpg
            //第三个 文件输入流
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, objectName, inputStream);
            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 如果上传成功，则返回200。
            System.out.println(result.getResponse().getStatusCode());
            System.out.println(result.getResponse().getErrorResponseAsString());
            System.out.println(result.getResponse().getUri());
            //返回上传图片在阿里云路径
            String url = result.getResponse().getUri();
            return url;
        } catch (Exception ce) {
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

    @Override
    public String uploadFileMinio(MultipartFile file) {
        //1.检查参数
        if (file==null || file.getSize()==0){
            return "失败";
        }

        //2.上传图片到minIO中
        String fileName = UUID.randomUUID().toString().replace("-", "");
        //aa.jpg
        String originalFilename = file.getOriginalFilename();
        String postFix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId=null;
        try {
            fileId=fileStorageService.uploadImgFile("",fileName+postFix,
                    file.getInputStream());
            log.info("上传图片到MinIO中，fileId:{}",fileId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("WmMaterialServiceImpl-上传文件失败");
        }

        //3.保存到数据库中

        //4.返回结果
        return fileId;
    }
}
