package com.xie.ssyx.home.controller;

import com.xie.ssyx.common.result.Result;
import com.xie.ssyx.common.security.AuthContextHolder;
import com.xie.ssyx.home.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @title: ItemApiController
 * @Author Xie
 * @Date: 2023/11/20 20:53
 * @Version 1.0
 */
@Api(tags = "商品详情")
@RestController
@RequestMapping("api/home")
public class ItemApiController {

    @Resource
    private ItemService itemService;

    @ApiOperation(value = "获取sku详细信息")
    @GetMapping("item/{id}")
    public Result index(@PathVariable Long id, HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId();
        return Result.ok(itemService.item(id, userId));
    }

}
