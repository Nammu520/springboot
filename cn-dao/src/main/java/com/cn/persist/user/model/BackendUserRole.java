package com.cn.persist.user.model;

import lombok.Data;

import java.util.Date;

@Data
public class BackendUserRole extends BackendUserRoleKey {
    private Boolean isActive;

    private Date createTime;

    private Date updateTime;
}