package com.xie.ssyx.activity.service.impl;

import com.aliyun.oss.common.utils.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.ssyx.activity.mapper.SeckillMapper;
import com.xie.ssyx.activity.mapper.SeckillTimeMapper;
import com.xie.ssyx.activity.service.SeckillSkuService;
import com.xie.ssyx.activity.service.SeckillTimeService;
import com.xie.ssyx.common.constant.RedisConst;
import com.xie.ssyx.model.activity.SeckillTime;
import com.xie.ssyx.vo.activity.SeckillSkuVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @title: SeckillTimeServiceImpl
 * @Author Xie
 * @Date: 2023/11/12 22:15
 * @Version 1.0
 */
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class SeckillTimeServiceImpl extends ServiceImpl<SeckillTimeMapper, SeckillTime> implements SeckillTimeService {

    @Resource
    private SeckillTimeMapper seckillTimeMapper;

    @Resource
    private SeckillSkuService seckillSkuService;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void updateStatus(Long id, Integer status) {
        SeckillTime seckillTime = new SeckillTime();
        seckillTime.setId(id);
        seckillTime.setStatus(status);
        this.updateById(seckillTime);
    }

    @Override
    public void saveSeckillTimeListToCache() {
        LambdaQueryWrapper<SeckillTime> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SeckillTime::getStatus, 1);
        List<SeckillTime> seckillTimeList = seckillTimeMapper.selectList(queryWrapper);

        //以场次名称为key，场次信息为value保存数据到缓存
        Map<String, SeckillTime> nameToSeckillTimeMap = new HashMap(  );
        for(SeckillTime seckillTime : seckillTimeList){
            nameToSeckillTimeMap.put( seckillTime.getName(), seckillTime );
        }
        redisTemplate.boundHashOps(RedisConst.SECKILL_TIME_MAP).putAll(nameToSeckillTimeMap);
        //设置过期时间
        //redisTemplate.expire(RedisConst.SECKILL_TIME_MAP, DateUtil.getCurrentExpireTimes(), TimeUnit.SECONDS);
    }

    @Override
    public List<SeckillTime> findAllSeckillTimeListFromCache() {
        List<SeckillTime> seckillTimeList = redisTemplate.boundHashOps(RedisConst.SECKILL_TIME_MAP).values();
        /*if(null == seckillTimeList){
            return new ArrayList<>(  );
        }

        //设置当前时间的秒杀场次，开场时间降序排列，秒杀结束时间统一为一个时间点（23:00:00）
        List<SeckillTime> seckillTimeDescSortList = seckillTimeList.stream().sorted(Comparator.comparing(SeckillTime::getStartTime).reversed()).collect( Collectors.toList());
        //Date currentDate = DateUtil.parseTime(new DateTime().toString("HH:mm:ss"));
        boolean isFirst = true;
        for(SeckillTime seckillTime : seckillTimeDescSortList) {
            //场次状态 1：已开抢 2：抢购中 3：即将开抢
            if(DateUtil.dateCompare(seckillTime.getStartTime(), currentDate)) {
                //降序，第一个满足条件的为抢购中
                if(isFirst) {
                    //抢购中
                    seckillTime.setTimeStaus(2);
                    isFirst = false;
                } else {
                    //已开抢
                    seckillTime.setTimeStaus(1);
                }
            } else {
                //即将开抢
                seckillTime.setTimeStaus(3);
            }
        }
        //页面显示为升序，以开场时间排序升序排列
        List<SeckillTime> seckillTimeAscSortList = seckillTimeList.stream().sorted(Comparator.comparing(SeckillTime::getStartTime)).collect( Collectors.toList());*/
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> findHomeData() {
        List<SeckillTime> seckillTimeList = this.findAllSeckillTimeListFromCache();
        //如果没有到秒杀开始时间段，则获取第一个”即将开抢“时间段场次数据
        //如果已经开始秒杀，则获取秒杀中的时间段场次数据
        //获取秒杀中的数据
        List<SeckillTime> runingSeckillTimeList = seckillTimeList.stream().filter(seckillTime -> seckillTime.getTimeStaus() == 2).collect(Collectors.toList());
        //当前秒杀场次
        SeckillTime currentReckillTime = null;
        if(!CollectionUtils.isEmpty(runingSeckillTimeList)) {
            //存在秒杀中场次
            currentReckillTime = runingSeckillTimeList.get(0);
        } else {
            //不存在秒杀中场次，则取第一个”即将开抢“时间段场次数据
            currentReckillTime = seckillTimeList.get(0);
        }

        //获取场次对应的商品信息
        List<SeckillSkuVo> seckillSkuVoList = seckillSkuService.findSeckillSkuListFromCache(currentReckillTime.getName());
        if(seckillSkuVoList.size() > 0) {
            seckillSkuVoList.add(seckillSkuVoList.get(0));
            seckillSkuVoList.add(seckillSkuVoList.get(0));
            seckillSkuVoList.add(seckillSkuVoList.get(0));
            seckillSkuVoList.add(seckillSkuVoList.get(0));
            seckillSkuVoList.add(seckillSkuVoList.get(0));
        }

        //返回当前场次与对应的商品信息
        Map<String, Object> result = new HashMap<>();
        result.put("seckillTime", currentReckillTime);
        result.put("seckillSkuVoList", seckillSkuVoList);
        return result;
    }
}
