package com.xie.ssyx.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.ssyx.model.sys.Region;

import java.util.List;

public interface RegionService extends IService<Region> {
    List<Region> findRegionByKeyword(String keyword);
}
