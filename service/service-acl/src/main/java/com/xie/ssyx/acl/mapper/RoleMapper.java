package com.xie.ssyx.acl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.ssyx.model.acl.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapper extends BaseMapper<Role> {

}
