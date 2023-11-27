package com.xie.ssyx.activity.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.ssyx.activity.service.SeckillService;
import com.xie.ssyx.common.result.Result;
import com.xie.ssyx.model.activity.Seckill;
import com.xie.ssyx.vo.activity.SeckillQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title: SeckillController
 * @Author Xie
 * @Date: 2023/11/12 22:07
 * @Version 1.0
 */

@Api(value = "Seckill管理", tags = "Seckill管理")
@RestController
@RequestMapping(value="/admin/activity/seckill")
@SuppressWarnings({"unchecked", "rawtypes"})
//@CrossOrigin
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "seckillQueryVo", value = "查询对象", required = false)
            SeckillQueryVo seckillQueryVo) {
        Page<Seckill> pageParam = new Page<>(page, limit);
        IPage<Seckill> pageModel = seckillService.selectPage(pageParam, seckillQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Seckill seckill = seckillService.getById(id);
        return Result.ok(seckill);
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody Seckill seckill) {
        seckillService.save(seckill);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody Seckill seckill) {
        seckillService.updateById(seckill);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        seckillService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        seckillService.removeByIds(idList);
        return Result.ok(null);
    }

    @ApiOperation(value = "更新状态")
    @PostMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        seckillService.updateStatus(id, status);
        return Result.ok(null);
    }
}
