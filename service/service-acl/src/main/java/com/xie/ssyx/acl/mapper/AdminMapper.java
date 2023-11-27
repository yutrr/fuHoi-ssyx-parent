package com.xie.ssyx.acl.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.ssyx.model.acl.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper extends BaseMapper<Admin> {
}
