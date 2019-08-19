package com.cn.persist.user.model;

import lombok.Data;

import java.util.Date;

@Data
public class Menus {
    private Integer id;

    private String name;

    private Byte level;

    private String url;

    private String code;

    private String pCode;

    private Byte type;

    private String remarks;

    private Boolean isActive;

    private Date createTime;

    private Date updateTime;
}