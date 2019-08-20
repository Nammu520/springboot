package com.cn.persist.user.model;

import lombok.Data;

import java.util.Date;

@Data
public class BackendUser {
    private Integer id;

    private String username;

    private String nickname;

    private String phone;

    private String password;

    private String remarks;

    private Boolean isActive;

    private Date createTime;

    private Date updateTime;
}