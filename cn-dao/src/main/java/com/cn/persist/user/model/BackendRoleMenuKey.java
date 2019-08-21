package com.cn.persist.user.model;

import lombok.Data;

@Data
public class BackendRoleMenuKey {
    private Integer roleId;

    private Integer menuId;

    public BackendRoleMenuKey(Integer roleId, Integer menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}