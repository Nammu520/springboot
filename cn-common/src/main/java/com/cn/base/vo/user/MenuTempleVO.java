package com.cn.base.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("菜单模板")
public class MenuTempleVO implements Serializable {

    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id", example = "1")
    private Integer id;

    /**
     * 给页面元素分配的唯一标识
     */
    @ApiModelProperty(value = "菜单唯一code", example = "001001")
    private String code;

    /**
     * 父级code
     */
    @ApiModelProperty(name = "p_code", value = "父级code", example = "001")
    private String pCode;

    /**
     * 功能中文名
     */
    @ApiModelProperty(value = "功能中文名", example = "设置")
    private String name;

    /**
     * 菜单级别
     */
    @ApiModelProperty(value = "菜单级别", example = "1")
    private Integer level;

    /**
     * 是否勾选该功能 0:否 1:是
     */
    @ApiModelProperty(name = "check_flag", value = "是否勾选该功能 0:否 1:是", example = "true")
    private Boolean checkFlag;

    /**
     * 功能对应子功能
     */
    @ApiModelProperty(value = "功能对应子功能")
    private List<MenuTempleVO> children;
}
