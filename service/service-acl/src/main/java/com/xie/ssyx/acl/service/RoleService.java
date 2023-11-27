package com.xie.ssyx.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.ssyx.model.acl.Role;
import com.xie.ssyx.vo.acl.RoleQueryVo;

import java.util.Map;

public interface RoleService extends IService<Role> {
    IPage<Role> selectPage(Page<Role> pageParam, RoleQueryVo roleQueryVo);

    /**
     * 分配角色
     * @param adminId
     * @param roleIds
     */
    void saveUserRoleRealtionShip(Long adminId, Long[] roleIds);

    /**
     * 根据用户获取角色数据
     * @param adminId
     * @return
     */
    Map<String, Object> findRoleByUserId(Long adminId);
}
