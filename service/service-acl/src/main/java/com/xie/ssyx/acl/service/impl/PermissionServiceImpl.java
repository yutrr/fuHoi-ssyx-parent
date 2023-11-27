package com.xie.ssyx.acl.service.impl;

/**
 * @title: PermissionServiceImpl
 * @Author Xie
 * @Date: 2023/9/15 21:34
 * @Version 1.0
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.ssyx.acl.helper.PermissionHelper;
import com.xie.ssyx.acl.mapper.PermissionMapper;
import com.xie.ssyx.acl.service.PermissionService;
import com.xie.ssyx.model.acl.Permission;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限服务实现类
 * </p>
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
        implements PermissionService {
    @Override
    public List<Permission> queryAllMenu() {
        //获取全部权限数据
        List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id as SIGNED)"));

        //把权限数据构建成树形结构数据
        List<Permission> result = PermissionHelper.build(allPermissionList);
        return result;
    }

    @Override
    public boolean removeChildById(Long id) {
        List<Long> idList =new ArrayList<>();
        this.selectChildListById(id,idList);
        idList.add(id);
        baseMapper.deleteBatchIds(idList);
        return true;
    }

    /**
     *	递归获取子节点
     * @param id
     * @param idList
     */
    private void selectChildListById(Long id,List<Long> idList) {
        List<Permission> childList = baseMapper.selectList(new QueryWrapper<Permission>().eq("pid", id).select("id"));
        childList.stream().forEach(item->{
            idList.add(item.getId());
            this.selectChildListById(item.getId(), idList);
        });

    }
}
