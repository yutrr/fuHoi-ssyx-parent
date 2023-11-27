package com.xie.ssyx.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.ssyx.common.exception.SSYXException;
import com.xie.ssyx.common.result.ResultCodeEnum;
import com.xie.ssyx.model.sys.RegionWare;
import com.xie.ssyx.sys.mapper.RegionWareMapper;
import com.xie.ssyx.sys.service.RegionWareService;
import com.xie.ssyx.vo.sys.RegionWareQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @title: ReginWareServiceImpl
 * @Author Xie
 * @Date: 2023/9/18 21:45
 * @Version 1.0
 */

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class RegionWareServiceImpl  extends ServiceImpl<RegionWareMapper, RegionWare> implements RegionWareService {

    @Resource
    private RegionWareMapper regionWareMapper;
    @Override
    public IPage<RegionWare> selectPage(Page<RegionWare> pageParam, RegionWareQueryVo regionWareQueryVo) {
        String keyword=regionWareQueryVo.getKeyword();
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)){
            wrapper.like(RegionWare::getRegionName,keyword)
                    .or().like(RegionWare::getWareName,keyword);
        }

        IPage<RegionWare> regionWarePage = baseMapper.selectPage(pageParam, wrapper);


        return regionWarePage;
    }

    //添加开通区域
    @Override
    public void saveRegionWare(RegionWare regionWare) {
        LambdaQueryWrapper<RegionWare> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegionWare::getRegionId,regionWare.getRegionId());
        Integer count = regionWareMapper.selectCount(queryWrapper);
        if (count>0){
            throw new SSYXException(ResultCodeEnum.REGION_OPEN);
        }
        baseMapper.insert(regionWare);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        RegionWare regionWare = baseMapper.selectById(id);
        regionWare.setStatus(status);
        baseMapper.updateById(regionWare);
    }
}
