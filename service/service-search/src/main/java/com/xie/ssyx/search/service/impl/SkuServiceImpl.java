package com.xie.ssyx.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.xie.ssyx.activity.client.ActivityFeignClient;
import com.xie.ssyx.client.product.ProductFeignClient;
import com.xie.ssyx.common.security.AuthContextHolder;
import com.xie.ssyx.enums.SkuType;
import com.xie.ssyx.model.product.Category;
import com.xie.ssyx.model.product.SkuInfo;
import com.xie.ssyx.model.search.SkuEs;
import com.xie.ssyx.search.repository.SkuRepository;
import com.xie.ssyx.search.service.SkuService;
import com.xie.ssyx.vo.search.SkuEsQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @title: SkuServiceImpl
 * @Author Xie
 * @Date: 2023/11/7 21:27
 * @Version 1.0
 */
@Slf4j
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private ActivityFeignClient activityFeignClient;
    @Autowired
    private SkuRepository skuEsRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 上架商品列表
     * @param skuId
     */
    @Override
    public void upperSku(Long skuId) {
        log.info("upperSku："+skuId);
        SkuEs skuEs = new SkuEs();

        //查询sku信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if(null == skuInfo) return;

        // 查询分类
        Category category = productFeignClient.getCategory(skuInfo.getCategoryId());
        if (category != null) {
            skuEs.setCategoryId(category.getId());
            skuEs.setCategoryName(category.getName());
        }
        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName()+","+skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if(skuInfo.getSkuType() == SkuType.COMMON.getCode()) {
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setStock(skuInfo.getStock());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        } else {
            //TODO 待完善-秒杀商品

        }
        SkuEs save = skuEsRepository.save(skuEs);
        log.info("upperSku："+ JSON.toJSONString(save));
    }

    /**
     * 下架商品列表
     * @param skuId
     */
    @Override
    public void lowerSku(Long skuId) {
        this.skuEsRepository.deleteById(skuId);
    }

    @Override
    public List<SkuEs> findHotSkuList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SkuEs> pageModel = skuEsRepository.findByOrderByHotScoreDesc(pageable);
        List<SkuEs> skuEsList = pageModel.getContent();
        return skuEsList;
    }

    @Override
    public Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo) {
        skuEsQueryVo.setWareId(AuthContextHolder.getWareId());
        Page<SkuEs> page =null;
        if (StringUtils.isEmpty(skuEsQueryVo.getKeyword())){
            page= skuEsRepository.findByCategoryIdAndWareId(
                    skuEsQueryVo.getCategoryId(),
                    skuEsQueryVo.getWareId(),
                    pageable);
        }else {
            page= skuEsRepository.findByKeywordAndWareId(
                    skuEsQueryVo.getKeyword(),
                    skuEsQueryVo.getWareId(),
                    pageable);
        }

        List<SkuEs> skuEsList = page.getContent();
        //获取sku对应的促销活动标签
        if (!CollectionUtils.isEmpty(skuEsList)){
            List<Long> skuIdList = skuEsList.stream().map(SkuEs::getId).collect(Collectors.toList());
            //根据skuId远程调用service-activity里面的接口得到数据
            Map<Long,List<String>> skuIdToRuleListMap= activityFeignClient.findActivity(skuIdList);
            if (null!=skuIdToRuleListMap){
                skuEsList.forEach(skuEs-> skuEs.setRuleList(skuIdToRuleListMap.get(skuEs.getId())));
            }
        }

        return page;
    }

    @Override
    public void incrHotScore(Long skuId) {
        // 定义key
        String hotKey = "hotScore";
        // 保存数据
        Double hotScore = redisTemplate.opsForZSet().incrementScore(hotKey, "skuId:" + skuId, 1);
        if (hotScore%10==0){
            // 更新es
            Optional<SkuEs> optional = skuEsRepository.findById(skuId);
            SkuEs skuEs = optional.get();
            skuEs.setHotScore(Math.round(hotScore));
            skuEsRepository.save(skuEs);
        }
    }
}
