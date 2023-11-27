package com.xie.ssyx.acl.controller;

/**
 * @title: PermissionAdminController
 * @Author Xie
 * @Date: 2023/9/15 21:35
 * @Version 1.0
 */

import com.xie.ssyx.acl.service.PermissionService;
import com.xie.ssyx.common.result.Result;
import com.xie.ssyx.model.acl.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理 前端控制器
 */
@RestController
@RequestMapping("/admin/acl/permission")
@Api(tags = "菜单管理")
//@CrossOrigin //跨域
public class PermissionAdminController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "获取菜单")
    @GetMapping
    public Result index() {
        List<Permission> list = permissionService.queryAllMenu();
        return Result.ok(list);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public Result updateById(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return Result.ok(null);
    }

    @ApiOperation(value = "递归删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        permissionService.removeChildById(id);
        return Result.ok(null);
    }
}
