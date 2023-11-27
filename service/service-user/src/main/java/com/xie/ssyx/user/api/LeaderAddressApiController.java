package com.xie.ssyx.user.api;

import com.xie.ssyx.user.service.UserService;
import com.xie.ssyx.vo.user.LeaderAddressVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @title: LeaderAddressApiController
 * @Author Xie
 * @Date: 2023/11/18 19:34
 * @Version 1.0
 */

@Api(tags = "团长接口")
@RestController
@RequestMapping("/api/user/leader")
public class LeaderAddressApiController {

    @Autowired
    private UserService userService;


    @ApiOperation("提货点地址信息")
    @GetMapping("/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressByUserId(@PathVariable(value = "userId") Long userId) {
        return userService.getLeaderAddressByUserId(userId);
    }


}
