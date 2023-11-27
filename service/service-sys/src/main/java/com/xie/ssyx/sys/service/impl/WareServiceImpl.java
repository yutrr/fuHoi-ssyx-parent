package com.xie.ssyx.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.ssyx.model.sys.RegionWare;
import com.xie.ssyx.model.sys.Ware;
import com.xie.ssyx.sys.mapper.RegionWareMapper;
import com.xie.ssyx.sys.mapper.WareMapper;
import com.xie.ssyx.sys.service.RegionWareService;
import com.xie.ssyx.sys.service.WareService;
import org.springframework.stereotype.Service;

/**
 * @title: WareServiceImpl
 * @Author Xie
 * @Date: 2023/9/18 21:45
 * @Version 1.0
 */
@Service
public class WareServiceImpl extends ServiceImpl<WareMapper, Ware> implements WareService {
}
