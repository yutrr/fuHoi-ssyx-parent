package com.xie.ssyx.common.security;

import com.xie.ssyx.common.constant.RedisConst;
import com.xie.ssyx.common.utils.helper.JwtHelper;
import com.xie.ssyx.vo.user.UserLoginVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @title: UserLoginInterceptor
 * @Author Xie
 * @Date: 2023/11/17 21:14
 * @Version 1.0
 */
public class UserLoginInterceptor implements HandlerInterceptor {

    private RedisTemplate redisTemplate;

    public  UserLoginInterceptor(RedisTemplate redisTemplate){
        this.redisTemplate=redisTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       this.initUserLoginVo(request);
       return true;
    }

    private void initUserLoginVo(HttpServletRequest request){
        //从请求头获取token
        String token = request.getHeader("token");
        System.out.println("token = " + token);
        if (!StringUtils.isEmpty(token)){
            Long userId = JwtHelper.getUserId(token);
            UserLoginVo userLoginVo = (UserLoginVo)redisTemplate.opsForValue()
                    .get(RedisConst.USER_LOGIN_KEY_PREFIX + userId);
            if (userLoginVo!=null){
                //将userInfo放入上下文中
                AuthContextHolder.setUserId(userLoginVo.getUserId());
                AuthContextHolder.setWareId(userLoginVo.getWareId());
                AuthContextHolder.setUserLoginVo(userLoginVo);
            }
        }
    }
}
