package com.cn.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PageReqDTO implements Serializable {

    @ApiModelProperty(name = "page_num", value = "页码（第几页）")
    private Integer pageNum = 1;

    @ApiModelProperty(name = "page_size", value = "每页大小")
    private Integer pageSize = 10;
}