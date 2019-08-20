package com.cn.persist.user.model;

import lombok.Data;

import java.util.Date;

@Data
public class BackendRole {
    private Integer id;

    private String name;

    private Boolean isActive;

    private Date createTime;

    private Date updateTime;
}