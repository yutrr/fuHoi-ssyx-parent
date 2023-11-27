package com.xie.ssyx.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xie.ssyx.enums.UserType;
import com.xie.ssyx.model.user.Leader;
import com.xie.ssyx.model.user.User;
import com.xie.ssyx.model.user.UserDelivery;
import com.xie.ssyx.user.mapper.LeaderMapper;
import com.xie.ssyx.user.mapper.UserDeliveryMapper;
import com.xie.ssyx.user.mapper.UserMapper;
import com.xie.ssyx.user.service.UserService;
import com.xie.ssyx.vo.user.LeaderAddressVo;
import com.xie.ssyx.vo.user.UserLoginVo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @title: UserServiceImpl
 * @Author Xie
 * @Date: 2023/11/13 22:30
 * @Version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

   @Autowired
   private UserDeliveryMapper userDeliveryMapper;

   @Autowired
   private LeaderMapper leaderMapper;
    @Override
    public User getUserByOpenId(String openId) {
        User user = baseMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getOpenId, openId));
        return user;
    }

    @Override
    public LeaderAddressVo getLeaderAddressByUserId(Long userId) {
        UserDelivery userDelivery = userDeliveryMapper.selectOne(
                new LambdaQueryWrapper<UserDelivery>()
                        .eq(UserDelivery::getUserId, userId)
                        .eq(UserDelivery::getIsDefault, 1));
        //拿着上面查询团长id查询leader表查询团长其他信息
        if (userDelivery==null)return null;
        Leader leader = leaderMapper.selectById(userDelivery.getLeaderId());
        LeaderAddressVo leaderAddressVo = new LeaderAddressVo();
        BeanUtils.copyProperties(leader, leaderAddressVo);
        leaderAddressVo.setUserId(userId);
        leaderAddressVo.setLeaderId(leader.getId());
        leaderAddressVo.setLeaderName(leader.getName());
        leaderAddressVo.setLeaderPhone(leader.getPhone());
        leaderAddressVo.setWareId(userDelivery.getWareId());
        leaderAddressVo.setStorePath(leader.getStorePath());
        return leaderAddressVo;
    }

    @Override
    public UserLoginVo getUserLoginVo(Long userId) {
        UserLoginVo userLoginVo = new UserLoginVo();
        User user = this.getById(userId);
        userLoginVo.setNickName(user.getNickName());
        userLoginVo.setUserId(userId);
        userLoginVo.setPhotoUrl(user.getPhotoUrl());
        userLoginVo.setOpenId(user.getOpenId());
        userLoginVo.setIsNew(user.getIsNew());

        LambdaQueryWrapper<UserDelivery> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDelivery::getUserId, userId);
        queryWrapper.eq(UserDelivery::getIsDefault, 1);
        UserDelivery userDelivery = userDeliveryMapper.selectOne(queryWrapper);
        if(null != userDelivery) {
            userLoginVo.setLeaderId(userDelivery.getLeaderId());
            userLoginVo.setWareId(userDelivery.getWareId());
        } else {
            userLoginVo.setLeaderId(1L);
            userLoginVo.setWareId(1L);
        }

        //如果是团长获取当前前团长id与对应的仓库id
        /*if(user.getUserType() == UserType.LEADER) {
            LambdaQueryWrapper<Leader> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Leader::getUserId, userId);
            queryWrapper.eq(Leader::getCheckStatus, 1);
            Leader leader = leaderMapper.selectOne(queryWrapper);
            if(null != leader) {
                userLoginVo.setLeaderId(leader.getId());
                Long wareId = regionFeignClient.getWareId(leader.getRegionId());
                userLoginVo.setWareId(wareId);
            }
        } else {
            //如果是会员获取当前会员对应的仓库id
            LambdaQueryWrapper<UserDelivery> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserDelivery::getUserId, userId);
            queryWrapper.eq(UserDelivery::getIsDefault, 1);
            UserDelivery userDelivery = userDeliveryMapper.selectOne(queryWrapper);
            if(null != userDelivery) {
                userLoginVo.setLeaderId(userDelivery.getLeaderId());
                userLoginVo.setWareId(userDelivery.getWareId());
            } else {
                userLoginVo.setLeaderId(1L);
                userLoginVo.setWareId(1L);
            }
        }*/
        return userLoginVo;
    }
}
