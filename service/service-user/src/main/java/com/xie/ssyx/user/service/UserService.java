package com.xie.ssyx.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.ssyx.model.user.User;
import com.xie.ssyx.vo.user.LeaderAddressVo;
import com.xie.ssyx.vo.user.UserLoginVo;

public interface UserService extends IService<User> {
    /**
     * 根据微信openid获取用户信息
     * @param openId
     * @return
     */
    User getUserByOpenId(String openId);


    //提货点地址信息
    LeaderAddressVo getLeaderAddressByUserId(Long userId);
    /**
     * 获取当前登录用户信息
     * @param userId
     * @return
     */
    UserLoginVo getUserLoginVo(Long userId);
}
