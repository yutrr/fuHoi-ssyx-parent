package com.xie.ssyx.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xie.ssyx.model.activity.ActivityInfo;
import com.xie.ssyx.model.activity.ActivityRule;
import com.xie.ssyx.model.activity.ActivitySku;
import com.xie.ssyx.model.activity.SeckillTime;
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
public interface SeckillTimeMapper extends BaseMapper<SeckillTime> {
}
