package com.xie.ssyx.acl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.ssyx.acl.mapper.AdminRoleMapper;
import com.xie.ssyx.acl.service.AdminRoleService;
import com.xie.ssyx.model.acl.AdminRole;
import org.springframework.stereotype.Service;

/**
 * @title: AdminRoleServiceImpl
 * @Author Xie
 * @Date: 2023/9/14 22:13
 * @Version 1.0
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole>
        implements AdminRoleService {
}
