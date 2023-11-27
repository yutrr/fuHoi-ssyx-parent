package com.xie.ssyx.common.config;

import com.xie.ssyx.common.exception.MinioException;
import com.xie.ssyx.common.service.FileStorageService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


//@Data
@Configuration
//@ConfigurationProperties(prefix = "minio")
//@ConditionalOnClass({MinioClient.class})
//@ConditionalOnProperty({"minio.endpoint"})
@EnableConfigurationProperties({MinIOConfigProperties.class})
//当引入FileStorageService接口时
@ConditionalOnClass(FileStorageService.class)
public class MinIOConfig {

    /*@Autowired
    private Environment environment;

    public MinIOConfig(){

    }

    @Bean
    public MinioClient minioClient(){
        String endpoint = this.environment.getProperty("minio.endpoint");
        String accessKey = this.environment.getProperty("minio.accessKey");
        String secretKey = this.environment.getProperty("minio.secretKey");
        String bucketName = this.environment.getProperty("minio.bucketName");
        if (endpoint !=null &&!"".equals(endpoint)){
            if (accessKey !=null &&!"".equals(accessKey)){
                if (secretKey !=null &&!"".equals(secretKey)){
                    if (bucketName !=null &&!"".equals(bucketName)){
                        MinioClient minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
                        this.makeBucket(minioClient,bucketName);
                        return minioClient;
                    }else {
                        throw new MinioException("存储桶名称未在application.yml配置",500);
                    }
                }else {
                    throw new MinioException("Minio密码未在application.yml配置",500);
                }
            }else {
                throw new MinioException("Minio名称未在application.yml配置",500);
            }
        }else {
            throw new MinioException("Minio的Url未在application.yml配置",500);
        }
    }

    private void makeBucket(MinioClient minioClient, String bucketName) {
        try {
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                String policyJson="Bucket already exists";
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policyJson).build());

            }
        }catch (Exception e){
            throw new MinioException("创建minio存储桶异常",500);
        }
    }
*/
    @Autowired
    private MinIOConfigProperties minIOConfigProperties;

    @Bean
    public MinioClient buildMinioClient() {
        return MinioClient
                .builder()
                .credentials(minIOConfigProperties.getAccessKey(), minIOConfigProperties.getSecretKey())
                .endpoint(minIOConfigProperties.getEndpoint())
                .build();
    }
}