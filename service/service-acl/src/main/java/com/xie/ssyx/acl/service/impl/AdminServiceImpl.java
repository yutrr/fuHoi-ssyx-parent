package com.xie.ssyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.ssyx.acl.mapper.AdminMapper;
import com.xie.ssyx.acl.service.AdminService;
import com.xie.ssyx.acl.service.RoleService;
import com.xie.ssyx.model.acl.Admin;
import com.xie.ssyx.vo.acl.AdminQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @title: AdminServiceImpl
 * @Author Xie
 * @Date: 2023/9/5 20:48
 * @Version 1.0
 */
/**
 * <p>
 * 用户角色服务实现类
 * </p>
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RoleService roleService;

    @Override
    public IPage<Admin> selectPage(Page<Admin> pageParam, AdminQueryVo userQueryVo) {
        //获取用户名称条件值
        String name = userQueryVo.getName();
        //创建条件构造器
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(name)) {
            //封装条件
            wrapper.like(Admin::getName,name);
        }
        //调用mapper方法
        IPage<Admin> pageModel = baseMapper.selectPage(pageParam,wrapper);
        return pageModel;
    }
}
