package com.xie.ssyx.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.ssyx.model.acl.Admin;
import com.xie.ssyx.vo.acl.AdminQueryVo;

/**
 * <p>
 * 用户服务接口
 * </p>
 */
public interface AdminService extends IService<Admin> {

    /**
     * 用户分页列表
     * @param pageParam
     * @param userQueryVo
     * @return
     */
    IPage<Admin> selectPage(Page<Admin> pageParam, AdminQueryVo userQueryVo);

}
