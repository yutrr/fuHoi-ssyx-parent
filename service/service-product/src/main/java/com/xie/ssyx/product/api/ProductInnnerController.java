package com.xie.ssyx.product.api;

import com.xie.ssyx.model.product.Category;
import com.xie.ssyx.model.product.SkuInfo;
import com.xie.ssyx.product.service.CategoryService;
import com.xie.ssyx.product.service.SkuInfoService;
import com.xie.ssyx.vo.product.SkuInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title: ProductInnnerController
 * @Author Xie
 * @Date: 2023/11/7 21:41
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/product")
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProductInnnerController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuInfoService skuInfoService;

    @ApiOperation(value = "根据分类id获取分类信息")
    @GetMapping("inner/getCategory/{categoryId}")
    public Category getCategory(@PathVariable Long categoryId) {
        return categoryService.getById(categoryId);
    }

    @ApiOperation(value = "根据skuId获取sku信息")
    @GetMapping("inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId) {
        return skuInfoService.getById(skuId);
    }

    //根据skuId列表得到sku信息列表
    @ApiOperation(value = "批量获取sku信息")
    @PostMapping("inner/findSkuInfoList")
    public List<SkuInfo> findSkuInfoList(@RequestBody List<Long> skuIdList) {
        return skuInfoService.findSkuInfoList(skuIdList);
    }

    /**
     * 批量获取分类信息
     * @param categoryIdList
     * @return
     */
    @PostMapping("inner/findCategoryList")
    List<Category> findCategoryList(@RequestBody List<Long> categoryIdList){
        return categoryService.listByIds(categoryIdList);
    }

    @ApiOperation(value = "根据关键字获取sku列表")
    @GetMapping("inner/findSkuInfoByKeyword/{keyword}")
    public List<SkuInfo> findSkuInfoByKeyword(@PathVariable("keyword") String keyword) {
        return skuInfoService.findSkuInfoByKeyword(keyword);
    }

    @ApiOperation(value = "获取分类信息")
    @GetMapping("inner/findAllCategoryList")
    public List<Category> findAllCategoryList() {
        return categoryService.findAllList();
    }

    @ApiOperation(value = "获取新人专享")
    @GetMapping("inner/findNewPersonSkuInfoList")
    public List<SkuInfo> findNewPersonSkuInfoList() {
        return skuInfoService.findNewPersonSkuInfoList();
    }

    //根据skuId获取sku信息(home)
    @GetMapping("inner/getSkuInfoVo/{skuId}")
    public SkuInfoVo getSkuInfoVo(@PathVariable Long skuId){
        return skuInfoService.getSkuInfoVo(skuId);
    }

}
