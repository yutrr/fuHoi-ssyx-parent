package com.xie.ssyx.common.security;

import com.xie.ssyx.vo.acl.AdminLoginVo;
import com.xie.ssyx.vo.user.UserLoginVo;

/**
 * @title: AuthContextHolder
 * @Author Xie
 * @Date: 2023/11/17 21:08
 * @Version 1.0
 */

/**
 * ThreadLocal工具类
 *
 */
public class AuthContextHolder {
    //用户id
    private static ThreadLocal<Long> userId= new ThreadLocal<>();

    //用户仓库id
    private static ThreadLocal<Long> wareId=new ThreadLocal<>();

    //用户信息对象
    private static ThreadLocal<UserLoginVo> userLoginVo=new ThreadLocal<>();

    //后台管理用户id
    private static ThreadLocal<Long> adminId = new ThreadLocal<Long>();
    //管理员基本信息
    private static ThreadLocal<AdminLoginVo> adminLoginVo = new ThreadLocal<>();

    public static Long getUserId(){
        return userId.get();
    }

    public static void setUserId(Long _userId){
        userId.set(_userId);
    }

    public static Long getWareId(){
        return wareId.get();
    }

    public static void setWareId(Long _wareId){
        wareId.set(_wareId);
    }

    public static UserLoginVo getUserLoginVo() {
        return userLoginVo.get();
    }

    public static void setUserLoginVo(UserLoginVo _userLoginVo) {
        userLoginVo.set(_userLoginVo);
    }

    public static Long getAdminId() {
        return adminId.get();
    }

    public static void setAdminId(Long _adminId) {
        adminId.set(_adminId);
    }

    public static AdminLoginVo getAdminLoginVo() {
        return adminLoginVo.get();
    }

    public static void setAdminLoginVo(AdminLoginVo _adminLoginVo) {
        adminLoginVo.set(_adminLoginVo);
    }

}
