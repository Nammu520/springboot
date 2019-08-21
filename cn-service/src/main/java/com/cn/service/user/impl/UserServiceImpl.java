package com.cn.service.user.impl;

import com.cn.base.vo.user.RoleMenuTreeVO;
import com.cn.persist.user.dao.BackendUserRoleMapper;
import com.cn.persist.user.model.BackendUserRole;
import com.cn.service.user.RoleService;
import com.cn.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private BackendUserRoleMapper userRoleMapper;

    @Override
    public RoleMenuTreeVO getUserPermissions(Integer userId){
        // 获取用户管理的角色
        BackendUserRole userRole = userRoleMapper.selectByUserId(userId);
        return roleService.getRoleDetail(userRole.getRoleId());
    }
}
