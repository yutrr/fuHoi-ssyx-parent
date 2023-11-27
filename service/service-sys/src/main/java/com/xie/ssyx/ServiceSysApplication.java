package com.xie.ssyx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @title: ServiceSysApplication
 * @Author Xie
 * @Date: 2023/9/18 21:41
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.xie.ssyx.sys.mapper")
public class ServiceSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSysApplication.class, args);
    }

}
