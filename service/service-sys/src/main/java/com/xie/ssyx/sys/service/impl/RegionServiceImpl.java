package com.xie.ssyx.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.ssyx.model.sys.Region;
import com.xie.ssyx.model.sys.RegionWare;
import com.xie.ssyx.sys.mapper.RegionMapper;
import com.xie.ssyx.sys.mapper.RegionWareMapper;
import com.xie.ssyx.sys.service.RegionService;
import com.xie.ssyx.sys.service.RegionWareService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @title: ReginServiceImpl
 * @Author Xie
 * @Date: 2023/9/18 21:44
 * @Version 1.0
 */

@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {
    //根据关键字获取地区列表
    @Override
    public List<Region> findRegionByKeyword(String keyword) {
        LambdaQueryWrapper<Region> queryWrapper = new LambdaQueryWrapper<Region>().like(Region::getName, keyword);
        return baseMapper.selectList(queryWrapper);
    }
}
