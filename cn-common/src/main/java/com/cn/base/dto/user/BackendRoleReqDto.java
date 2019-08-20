package com.cn.base.dto.user;

import com.cn.base.annotation.validation.MaxLength;
import com.cn.base.dto.PageReqDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BackendRoleReqDto extends PageReqDTO {

    @ApiModelProperty(name = "name", value = "角色名")
    @MaxLength(max = 10, message = "输入的角色名长度大于5")
    private String name;
}
