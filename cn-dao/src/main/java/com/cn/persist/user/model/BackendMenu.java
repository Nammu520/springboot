package com.cn.persist.user.model;

import lombok.Data;

import java.util.Date;

@Data
public class BackendMenu {
    private Integer id;

    private String name;

    private Integer level;

    private String url;

    private String code;

    private String pCode;

    private Integer type;

    private String remarks;

    private Boolean isActive;

    private Date createTime;

    private Date updateTime;
}