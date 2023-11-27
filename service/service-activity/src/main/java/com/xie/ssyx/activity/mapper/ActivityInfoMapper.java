package com.xie.ssyx.activity.mapper;

import com.xie.ssyx.model.activity.ActivityInfo;
import com.xie.ssyx.model.activity.ActivityRule;
import com.xie.ssyx.model.activity.ActivitySku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 活动表 Mapper 接口
 * </p>
 *
 * @author xie
 * @since 2023-04-07
 */
@Repository
public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {

    // 如果之前参加过，活动正在进行中，排除商品
    List<Long> selectExistSkuIdList(@Param("skuIdList") List<Long> skuIdList);

    //根据skuId进行查询，查询sku对应活动里面规则列表
    List<ActivityRule> findActivityRule(@Param("skuId") Long skuId);

    //根据所有skuId列表获取参与活动
    List<ActivitySku> selectCartActivity(@Param("skuIdList") List<Long> skuIdList);
}
