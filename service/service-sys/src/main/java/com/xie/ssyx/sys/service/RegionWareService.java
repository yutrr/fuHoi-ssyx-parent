package com.xie.ssyx.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.ssyx.model.sys.RegionWare;
import com.xie.ssyx.vo.sys.RegionWareQueryVo;

public interface RegionWareService extends IService<RegionWare> {
    IPage<RegionWare> selectPage(Page<RegionWare> pageParam, RegionWareQueryVo regionWareQueryVo);

    void saveRegionWare(RegionWare regionWare);

    void updateStatus(Long id, Integer status);
}
