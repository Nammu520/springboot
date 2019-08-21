package com.cn.service.user;

import com.cn.base.vo.user.RoleMenuTreeVO;

public interface UserService {

    /**
     * 获取用户权限
     *
     * @param userId 用户Id
     * @return
     */
    RoleMenuTreeVO getUserPermissions(Integer userId);

}
