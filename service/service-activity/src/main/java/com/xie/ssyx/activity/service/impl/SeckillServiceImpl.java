package com.xie.ssyx.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.ssyx.activity.mapper.SeckillMapper;
import com.xie.ssyx.activity.service.SeckillService;
import com.xie.ssyx.model.activity.Seckill;
import com.xie.ssyx.vo.activity.SeckillQueryVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @title: SeckillServiceImpl
 * @Author Xie
 * @Date: 2023/11/12 22:14
 * @Version 1.0
 */
@Service
public class SeckillServiceImpl extends ServiceImpl<SeckillMapper, Seckill> implements SeckillService {

    @Autowired
    private SeckillMapper seckillMapper;

    @Override
    public IPage<Seckill> selectPage(Page<Seckill> pageParam, SeckillQueryVo seckillQueryVo) {

        Integer status = seckillQueryVo.getStatus();
        String title = seckillQueryVo.getTitle();
        LambdaQueryWrapper<Seckill> wrapper = new LambdaQueryWrapper<>();
        if (null!=status){
            wrapper.eq(Seckill::getStatus,status);
        }

        if (!StringUtils.isEmpty(title)){
            wrapper.like(Seckill::getTitle,title);
        }
        IPage<Seckill> seckillIPage=baseMapper.selectPage(pageParam,wrapper);

        return seckillIPage;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Seckill seckill = new Seckill();
        seckill.setStatus(status);
        seckill.setId(id);
        this.updateById(seckill);
    }
}
