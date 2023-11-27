package com.xie.ssyx.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.xie.ssyx.common.constant.RedisConst;
import com.xie.ssyx.common.exception.SSYXException;
import com.xie.ssyx.common.result.Result;
import com.xie.ssyx.common.result.ResultCodeEnum;
import com.xie.ssyx.common.security.AuthContextHolder;
import com.xie.ssyx.common.utils.helper.JwtHelper;
import com.xie.ssyx.enums.UserType;
import com.xie.ssyx.model.user.User;
import com.xie.ssyx.user.service.UserService;
import com.xie.ssyx.user.utils.ConstantPropertiesUtil;
import com.xie.ssyx.user.utils.HttpClientUtils;
import com.xie.ssyx.vo.user.LeaderAddressVo;
import com.xie.ssyx.vo.user.UserLoginVo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @title: WeixinApiController
 * @Author Xie
 * @Date: 2023/11/13 22:29
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/user/weixin")
public class WeixinApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation(value = "微信登录获取openid(小程序)")
    @GetMapping("/wxLogin/{code}")
    public Result loginWeiXin(@PathVariable String code){
        //1.得到微信返回code临时票据值
        System.out.println("微信授权服务器回调。。。。。。" + code);
        if (StringUtils.isEmpty(code)){
            throw new SSYXException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //使用code和appid以及appscrect换取access_token
        StringBuffer baseAccessTokenUrl =new StringBuffer()
                .append("https://api.weixin.qq.com/sns/jscode2session")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&js_code=%s")
                .append("&grant_type=authorization_code");
        //2.拿着code + 小程序id + 小程序秘钥 请求微信接口服务
        //使用HttpClient工具请求

        String tokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);
        String result;
        try {
             result = HttpClientUtils.get(tokenUrl);
        }catch (Exception e){
            throw new SSYXException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        System.out.println("使用code换取的access_token结果 = " + result);

        //3.请求微信接口服务，返回两个值 session_key 和openId
        //openId是你微信唯一标识
        JSONObject resultJson = JSONObject.parseObject(result);
        if(resultJson.getString("errcode") != null){
            throw new SSYXException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        String accessToken = resultJson.getString("session_key");
        String openId = resultJson.getString("openid");

        //TODO 为了测试，openId写固定
//        String accessToken = "";
        //String openId = "odo3j4uGJf6Hl2FopkEOLGxr7LE4";
        //4.添加微信用户信息到数据库里面
        //操作user表
        //判断是否第一次使用微信授权登录：如何判断？openId
        User user = userService.getUserByOpenId(openId);
        if (user==null){
            user = new User();
            user.setOpenId(openId);
            user.setNickName(openId);
            user.setPhotoUrl("");
            user.setUserType(UserType.USER);
            user.setIsNew(0);
            userService.save(user);
        }

        //5.根据userId查询提货点和团长信息
        //提货点  user表  user_delivery表
        //团长 leader表
        LeaderAddressVo leaderAddressVo = userService.getLeaderAddressByUserId(user.getId());
        //6.JWT工具根据userId和userName生成token字符串
        Map<String,Object> map =new HashMap<>();
        String name = user.getNickName();
        map.put("user",user);
        map.put("leaderAddressVo",leaderAddressVo);
        String token = JwtHelper.createToken(user.getId(), name);
        map.put("token",token);


        //7.获取当前登录用户信息，放到redis里面，设置有效时间
       UserLoginVo userLoginVo= userService.getUserLoginVo(user.getId());
        //8.需要数据封装到map返回
        redisTemplate.opsForValue()
                .set(RedisConst.USER_LOGIN_KEY_PREFIX +user.getId(),
                        userLoginVo,
                        RedisConst.USERKEY_TIMEOUT,
                        TimeUnit.DAYS);
        return Result.ok(map);
    }

    @PostMapping("/auth/updateUser")
    @ApiOperation(value = "更新用户昵称与头像")
    public Result updateUser(@RequestBody User user) {
        User user1 = userService.getById(AuthContextHolder.getUserId());
        //把昵称更新为微信用户
        user1.setNickName(user.getNickName().replaceAll("[ue000-uefff]", "*"));
        user1.setPhotoUrl(user.getPhotoUrl());
        userService.updateById(user1);
        return Result.ok(null);
    }
}
