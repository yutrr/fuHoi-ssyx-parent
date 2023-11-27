package com.xie.ssyx.acl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.ssyx.model.acl.Permission;

import java.util.List;

/**
 * <p>
 * 权限服务接口
 * </p>
 */
public interface PermissionService extends IService<Permission> {

    //获取所有菜单列表
    List<Permission> queryAllMenu();

    //递归删除
    boolean removeChildById(Long id);
}
