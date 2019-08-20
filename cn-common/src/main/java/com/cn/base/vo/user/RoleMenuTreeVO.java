package com.cn.base.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("角色详情Vo")
public class RoleMenuTreeVO extends RoleVO{

    @ApiModelProperty(value = "角色拥有的权限树")
    private List<MenuTempleVO> menuTree;
}
