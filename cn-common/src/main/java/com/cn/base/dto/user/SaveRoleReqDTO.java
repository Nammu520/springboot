package com.cn.base.dto.user;

import com.cn.base.annotation.validation.NotBlank;
import com.cn.base.vo.user.MenuTempleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "保存角色对象")
public class SaveRoleReqDTO implements Serializable {

    @ApiModelProperty(value = "角色id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "角色名", example = "技术支持")
    @NotBlank(field = "name", message = "角色名不能为空")
    private String name;

    @ApiModelProperty(value = "角色拥有的权限树")
    private List<MenuTempleVO> menuTree;

}
