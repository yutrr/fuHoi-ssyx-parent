package com.xie.ssyx.home.service.impl;

import com.xie.ssyx.activity.client.ActivityFeignClient;
import com.xie.ssyx.client.product.ProductFeignClient;
import com.xie.ssyx.client.user.UserFeignClient;
import com.xie.ssyx.home.service.HomeService;
import com.xie.ssyx.model.product.Category;
import com.xie.ssyx.model.product.SkuInfo;
import com.xie.ssyx.model.search.SkuEs;
import com.xie.ssyx.search.client.SearchFeignClient;
import com.xie.ssyx.vo.user.LeaderAddressVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @title: HomeServiceImpl
 * @Author Xie
 * @Date: 2023/11/17 21:43
 * @Version 1.0
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private SearchFeignClient searchFeignClient;

    @Autowired
    private ActivityFeignClient activityFeignClient;

    @Override
    public Map<String, Object> homeData(Long userId) {

        Map<String, Object> result=new HashMap<>();
        //1.根据userId获取当前登录语句提货地址信息
        //远程调用service-user模块接口获取需要数据
        LeaderAddressVo leaderAddressVo = userFeignClient.getUserAddressByUserId(userId);
        result.put("leaderAddressVo",leaderAddressVo);

        //2.获取所有分类
        //远程调用service-product模块接口
        List<Category> categoryList = productFeignClient.findAllCategoryList();
        result.put("categoryList",categoryList);

        //3.获取新人专享商品
        //远程调用service-product模块接口
        List<SkuInfo> newPersonSkuInfoList =  productFeignClient.findNewPersonSkuInfoList();
        result.put("newPersonSkuInfoList", newPersonSkuInfoList);

        //TODO 获取用户首页秒杀数据

        //4.获取爆款商品
        //远程调用service-search模块接口
        List<SkuEs> hotSkuList = searchFeignClient.findHotSkuList();
        //获取sku对应的促销活动标签
        if (!CollectionUtils.isEmpty(hotSkuList)){
            List<Long> skuIdList = hotSkuList.stream().map(sku -> sku.getId()).collect(Collectors.toList());
            Map<Long,List<String>> skuIdToRuleListMap=
                    activityFeignClient.findActivity(skuIdList);
            if(null!=skuIdToRuleListMap){
                hotSkuList.forEach(skuEs ->{
                    skuEs.setRuleList(skuIdToRuleListMap.get(skuEs.getId()));
                });
            }
        }
        result.put("hotSkuList",hotSkuList);

        //5.封装获取数据到map集合，返回

        return result;
    }
}
