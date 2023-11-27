package com.xie.ssyx.sys.controller;

/**
 * @title: RegionWareController
 * @Author Xie
 * @Date: 2023/9/18 21:51
 * @Version 1.0
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.ssyx.common.result.Result;
import com.xie.ssyx.model.sys.RegionWare;
import com.xie.ssyx.sys.service.RegionWareService;
import com.xie.ssyx.vo.sys.RegionWareQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @title: ReginController
 * @Author Xie
 * @Date: 2023/9/18 21:43
 * @Version 1.0
 */
@Api(value = "RegionWare管理", tags = "RegionWare管理")
@RestController
@RequestMapping(value="/admin/sys/regionWare")
@SuppressWarnings({"unchecked", "rawtypes"})
//@CrossOrigin
public class RegionWareController {

    @Resource
    private RegionWareService regionWareService;

    //开通区域列表
    @ApiOperation(value = "获取开通区域列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "regionWareVo", value = "查询对象", required = false)
            RegionWareQueryVo regionWareQueryVo) {

        Page<RegionWare> pageParam = new Page<>(page, limit);

        IPage<RegionWare> pageModel = regionWareService.selectPage(pageParam, regionWareQueryVo);

        return Result.ok(pageModel);
    }

    //添加开通区域
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody RegionWare regionWare) {
        regionWareService.saveRegionWare(regionWare);
        return Result.ok(null);
    }


    //删除开通区域
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        regionWareService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "取消开通区域")
    @PostMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id,@PathVariable Integer status) {
        regionWareService.updateStatus(id, status);
        return Result.ok(null);
    }
}

