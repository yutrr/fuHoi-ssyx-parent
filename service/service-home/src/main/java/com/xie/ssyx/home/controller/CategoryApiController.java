package com.xie.ssyx.home.controller;

import com.xie.ssyx.client.product.ProductFeignClient;
import com.xie.ssyx.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @title: CategoryApiController
 * @Author Xie
 * @Date: 2023/11/19 11:56
 * @Version 1.0
 */
@Api(tags = "商品分类")
@RestController
@RequestMapping("api/home")
public class CategoryApiController {

    @Resource
    private ProductFeignClient productFeignClient;

    @ApiOperation(value = "获取分类信息")
    @GetMapping("category")
    public Result index() {
        return Result.ok(productFeignClient.findAllCategoryList());
    }
}
