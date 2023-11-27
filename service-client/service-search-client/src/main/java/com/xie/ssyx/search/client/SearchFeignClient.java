package com.xie.ssyx.search.client;

import com.xie.ssyx.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("service-search")
public interface SearchFeignClient {

    /**
     * 更新商品incrHotScore
     * @param skuId
     * @return
     */
    @GetMapping("/api/search/sku/inner/incrHotScore/{skuId}")
    Boolean incrHotScore(@PathVariable("skuId") Long skuId);

    /**
     * 获取爆品商品
     * @return
     */
    @GetMapping("/api/search/sku/inner/findHotSkuList")
    List<SkuEs> findHotSkuList();
}
