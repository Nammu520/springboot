package com.cn.persist.user.model;

import lombok.Data;

import java.util.Date;

@Data
public class BackendRoleMenu extends BackendRoleMenuKey {
    private Boolean isActive;

    private Date createTime;

    private Date updateTime;

    public BackendRoleMenu(Integer roleId, Integer menuId){
        super(roleId, menuId);
    }
}