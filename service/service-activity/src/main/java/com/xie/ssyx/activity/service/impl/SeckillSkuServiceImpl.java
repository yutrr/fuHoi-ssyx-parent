package com.xie.ssyx.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.ssyx.activity.mapper.SeckillSkuMapper;
import com.xie.ssyx.activity.service.SeckillSkuService;
import com.xie.ssyx.client.product.ProductFeignClient;
import com.xie.ssyx.common.constant.RedisConst;
import com.xie.ssyx.common.exception.SSYXException;
import com.xie.ssyx.common.result.ResultCodeEnum;
import com.xie.ssyx.enums.SkuType;
import com.xie.ssyx.model.activity.SeckillSku;
import com.xie.ssyx.model.product.SkuInfo;
import com.xie.ssyx.vo.activity.SeckillSkuQueryVo;
import com.xie.ssyx.vo.activity.SeckillSkuVo;
import com.xie.ssyx.vo.product.SkuStockLockVo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @title: SeckillSkuServiceImpl
 * @Author Xie
 * @Date: 2023/11/12 22:14
 * @Version 1.0
 */
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class SeckillSkuServiceImpl extends ServiceImpl<SeckillSkuMapper, SeckillSku> implements SeckillSkuService {

    @Autowired
    private SeckillSkuMapper seckillSkuMapper;

    @Resource
    private ProductFeignClient productFeignClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public IPage<SeckillSku> selectPage(Page<SeckillSku> pageParam, SeckillSkuQueryVo seckillSkuQueryVo) {
        LambdaQueryWrapper<SeckillSku> wrapper=new LambdaQueryWrapper<>();
        Long seckillId = seckillSkuQueryVo.getSeckillId();
        Long seckillTimeId = seckillSkuQueryVo.getSeckillTimeId();
        if (seckillId!=null){
            wrapper.eq(SeckillSku::getSeckillId,seckillSkuQueryVo.getSeckillId());
        }

        if (seckillTimeId!=null){
            wrapper.eq(SeckillSku::getSeckillTimeId,seckillSkuQueryVo.getSeckillTimeId());
        }
        IPage<SeckillSku> seckillSkuIPage=seckillSkuMapper.selectPage(pageParam,wrapper);
        if (CollectionUtils.isEmpty(seckillSkuIPage.getRecords())){
            return seckillSkuIPage;
        }
        List<Long> skuIdList =
                seckillSkuIPage.getRecords().stream().map(SeckillSku::getSkuId).collect(Collectors.toList());
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(skuIdList);
        Map<Long, SkuInfo> skuIdToSkuInfoMap = skuInfoList.stream().collect(Collectors.toMap(SkuInfo::getId, SkuInfo -> SkuInfo));
        seckillSkuIPage.getRecords().stream().forEach(item ->{
            item.setSkuInfo(skuIdToSkuInfoMap.get(item.getSkuId()));
        });
        return seckillSkuIPage;
    }

    @Override
    public void save(List<SeckillSku> seckillSkuList) {
        this.saveBatch(seckillSkuList);

        //更新sku为秒杀商品
        List<Long> skuIdList = seckillSkuList.stream().map(SeckillSku::getSkuId).collect(Collectors.toList());
        //productFeignClient.updateSkuType(skuIdList, SkuType.SECKILL);

    }

    @Override
    public void saveSeckillSkuListToCache(String date) {
        List<SeckillSkuVo> seckillSkuVoList =new ArrayList<>();//seckillSkuMapper.findSeckillSkuListByDate(date);

        for (SeckillSkuVo seckillSkuVo : seckillSkuVoList) {
            //将秒杀sku信息放入缓存

            redisTemplate.boundHashOps(RedisConst.SECKILL_SKU_MAP).put(seckillSkuVo.getSkuId().toString(),seckillSkuVo);
            //将库存数据保存到redis队列，防止超卖
            Integer seckillStock = seckillSkuVo.getSeckillStock();
            Long[] skuIdArray =new Long[seckillStock];
            for (int i = 0; i < seckillStock; i++) {
                skuIdArray[i] =seckillSkuVo.getSkuId();
            }
            redisTemplate.boundListOps(RedisConst.SECKILL_SKU_LIST + seckillSkuVo.getSkuId()).leftPushAll(skuIdArray);
            //设置过期时间
            //redisTemplate.expire(RedisConst.SECKILL_SKU_MAP, DateUtil.getCurrentExpireTimes(), TimeUnit.SECONDS);
        }

    }

    @Override
    public List<SeckillSkuVo> findSeckillSkuListFromCache(String timeName) {
        List<SeckillSkuVo> seckillSkuVoList = redisTemplate.boundHashOps(RedisConst.SECKILL_SKU_MAP).values();
        if(seckillSkuVoList.size() >1) {
            seckillSkuVoList.add(seckillSkuVoList.get(0));
            seckillSkuVoList.add(seckillSkuVoList.get(0));
            seckillSkuVoList.add(seckillSkuVoList.get(0));
            seckillSkuVoList.add(seckillSkuVoList.get(0));
        }
        return seckillSkuVoList;
        //return  seckillSkuVoList.stream().filter(seckillSkuVo -> timeName.equals( seckillSkuVo.getTimeName())).collect( Collectors.toList() );
    }

    @Override
    public SeckillSkuVo getSeckillSkuVo(Long skuId) {
        SeckillSkuVo seckillSkuVo = (SeckillSkuVo)redisTemplate.boundHashOps(RedisConst.SECKILL_SKU_MAP).get(skuId.toString());
        if(null == seckillSkuVo) {
            String date = new DateTime().toString("yyyy-MM-dd");
            this.saveSeckillSkuListToCache(date);
        }

        //场次状态 1：已开抢 2：抢购中 3：即将开抢
       /* Date currentDate = DateUtil.parseTime(new DateTime().toString("HH:mm:ss"));
        if(DateUtil.dateCompare(seckillSkuVo.getStartTime(), currentDate)) {
            seckillSkuVo.setTimeStaus(1);
        } else {
            //即将开抢
            seckillSkuVo.setTimeStaus(3);
        }*/
        return seckillSkuVo;
    }

    @Transactional
    @Override
    public Boolean checkAndMinusStock(List<SkuStockLockVo> skuStockLockVoList, String orderToken) {
        if (CollectionUtils.isEmpty(skuStockLockVoList)){
            throw new SSYXException(ResultCodeEnum.DATA_ERROR);
        }

        // 遍历所有商品，验库存并锁库存，要具备原子性
        skuStockLockVoList.forEach(skuStockLockVo -> {
            checkLock(skuStockLockVo);
        });

        // 只要有一个商品锁定失败，所有锁定成功的商品要解锁库存
        if (skuStockLockVoList.stream().anyMatch(skuStockLockVo -> !skuStockLockVo.getIsLock())) {
            // 获取所有锁定成功的商品，遍历解锁库存
            skuStockLockVoList.stream().filter(SkuStockLockVo::getIsLock).forEach(skuStockLockVo -> {
                //seckillSkuMapper.rollBackStock(skuStockLockVo.getSkuId(), skuStockLockVo.getSkuNum());
            });
            // 响应锁定状态
            return false;
        }

        // 如果所有商品都锁定成功的情况下，需要缓存锁定信息到redis。以方便将来解锁库存 或者 减库存
        // 以orderToken作为key，以lockVos锁定信息作为value
        this.redisTemplate.opsForValue().set(RedisConst.SROCK_INFO + orderToken, skuStockLockVoList);

        // 锁定库存成功之后，定时解锁库存。
        //this.rabbitTemplate.convertAndSend("ORDER_EXCHANGE", "stock.ttl", orderToken);
        return true;
    }

    //@GmallLock
    private void checkLock(SkuStockLockVo skuStockLockVo){
        SeckillSkuVo seckillSkuVo = this.getSeckillSkuVo(skuStockLockVo.getSkuId());
        if(null == seckillSkuVo) return;

        /*Integer result = seckillSkuMapper.minusStock(seckillSkuVo.getSeckillSkuId(), skuStockLockVo.getSkuNum());
        if (result == 1) {
            skuStockLockVo.setIsLock(true);
        }*/
    }

    @Transactional
    @Override
    public void rollBackStock(String orderNo) {
        // 获取锁定库存的缓存信息
        List<SkuStockLockVo> skuStockLockVoList = (List<SkuStockLockVo>)this.redisTemplate.opsForValue().get(RedisConst.SROCK_INFO + orderNo);
        if (CollectionUtils.isEmpty(skuStockLockVoList)){
            return ;
        }

        // 回滚库存
        skuStockLockVoList.forEach(skuStockLockVo -> {
            SeckillSkuVo seckillSkuVo = this.getSeckillSkuVo(skuStockLockVo.getSkuId());
            //seckillSkuMapper.rollBackStock(seckillSkuVo.getSeckillSkuId(), skuStockLockVo.getSkuNum());
        });

        // 回滚库存之后，删除锁定库存的缓存。以防止重复解锁库存
        this.redisTemplate.delete(RedisConst.SROCK_INFO + orderNo);
    }

    @Override
    public boolean removeById(Serializable id) {
        SeckillSku seckillSku = this.getById(id);
        seckillSkuMapper.deleteById(id);

        //更新sku为秒杀商品
        List<Long> skuIdList = new ArrayList<>();
        skuIdList.add(seckillSku.getSkuId());
        //productFeignClient.updateSkuType(skuIdList, SkuType.COMMON);
        return false;
    }
}
