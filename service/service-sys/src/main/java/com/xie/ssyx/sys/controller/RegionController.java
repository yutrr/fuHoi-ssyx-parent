package com.xie.ssyx.sys.controller;

/**
 * @title: ReginWareController
 * @Author Xie
 * @Date: 2023/9/18 21:43
 * @Version 1.0
 */

import com.xie.ssyx.common.result.Result;
import com.xie.ssyx.sys.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "区域接口")
@RestController
@RequestMapping("/admin/sys/region")
//@CrossOrigin
public class RegionController {

    @Resource
    private RegionService regionService;

    @ApiOperation(value = "根据关键字获取地区列表")
    @GetMapping("findRegionByKeyword/{keyword}")
    public Result findSkuInfoByKeyword(@PathVariable("keyword") String keyword) {
        return Result.ok(regionService.findRegionByKeyword(keyword));
    }
}
