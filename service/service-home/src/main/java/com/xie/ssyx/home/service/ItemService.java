package com.xie.ssyx.home.service;

import java.util.Map;

public interface ItemService {
    //获取sku详细信息
    Map<String, Object> item(Long skuId, Long userId);
}
