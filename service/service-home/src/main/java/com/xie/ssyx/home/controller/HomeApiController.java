package com.xie.ssyx.home.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xie.ssyx.common.result.Result;
import com.xie.ssyx.common.security.AuthContextHolder;
import com.xie.ssyx.home.service.HomeService;
import com.xie.ssyx.model.search.SkuEs;
import com.xie.ssyx.vo.search.SkuEsQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @title: HomeApiController
 * @Author Xie
 * @Date: 2023/11/17 21:42
 * @Version 1.0
 */
@Api(tags = "首页接口")
@RestController
@RequestMapping("api/home")
public class HomeApiController {

    @Autowired
    private HomeService homeService;

    @ApiOperation("首页数据显示接口")
    @GetMapping("index")
    public Result index(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId();
        Map<String,Object> map= homeService.homeData(userId);
        return Result.ok(map);
    }
}
