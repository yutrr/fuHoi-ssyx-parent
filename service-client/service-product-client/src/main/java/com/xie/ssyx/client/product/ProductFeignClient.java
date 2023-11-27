package com.xie.ssyx.client.product;

import com.xie.ssyx.enums.SkuType;
import com.xie.ssyx.model.product.Category;
import com.xie.ssyx.model.product.SkuInfo;
import com.xie.ssyx.vo.product.SkuInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "service-product")
public interface ProductFeignClient {

    @GetMapping("/api/product/inner/getCategory/{categoryId}")
     Category getCategory(@PathVariable("categoryId") Long categoryId);

    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
     SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId);

    /**
     * 批量获取sku信息
     * @param skuIdList
     * @return
     */
    @PostMapping("/api/product/inner/findSkuInfoList")
    List<SkuInfo> findSkuInfoList(@RequestBody List<Long> skuIdList);

    /**
     * 批量获取分类信息
     * @param categoryIdList
     * @return
     */
    @PostMapping("/api/product/inner/findCategoryList")
    List<Category> findCategoryList(@RequestBody List<Long> categoryIdList);

    /**
     * 根据关键字获取sku列表，活动使用
     * @param keyword
     * @return
     */
    @GetMapping("/api/product/inner/findSkuInfoByKeyword/{keyword}")
    List<SkuInfo> findSkuInfoByKeyword(@PathVariable("keyword") String keyword);

    //void updateSkuType(List<Long> skuIdList, SkuType seckill);

    /**
     * 获取分类信息
     * @return
     */
    @GetMapping("/api/product/inner/findAllCategoryList")
    List<Category> findAllCategoryList();

    /**
     * 获取新人专享
     * @return
     */
    @GetMapping("/api/product/inner/findNewPersonSkuInfoList")
    List<SkuInfo> findNewPersonSkuInfoList();

    @GetMapping("/api/product/inner/getSkuInfoVo/{skuId}")
     SkuInfoVo getSkuInfoVo(@PathVariable("skuId") Long skuId);
}
