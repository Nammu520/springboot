package com.cn.service.user;

import com.cn.base.dto.user.BackendUserLoginReqDTO;

public interface AuthService {

    /**
     * 检查用户是否登录
     *
     * @param ticket ticket
     * @return 是否存在
     */
    Integer checkUserLogin(String ticket);

    /**
     * 用户登出
     *
     * @param ticket ticket
     */
    void logout(String ticket);

    /**
     * 用户登录
     *
     * @param userLoginReqDto 用户登录对象
     */
    void login(BackendUserLoginReqDTO userLoginReqDto);
}
