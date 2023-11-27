package com.xie.ssyx.product.service;

import com.xie.ssyx.model.product.AttrGroup;
import com.xie.ssyx.vo.product.AttrGroupQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 属性分组 服务类
 * </p>
 *
 * @author xie
 * @since 2023-04-04
 */
public interface AttrGroupService extends IService<AttrGroup> {

    //平台属性分组列表
    IPage<AttrGroup> selectPageAttrGroup(Page<AttrGroup> pageParam, AttrGroupQueryVo attrGroupQueryVo);

    //查询所有平台属性分组列表
    List<AttrGroup> findAllListAttrGroup();
}
