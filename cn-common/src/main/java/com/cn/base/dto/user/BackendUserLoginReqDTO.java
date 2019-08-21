package com.cn.base.dto.user;

import com.cn.base.annotation.validation.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dengyu
 * @desc
 * @date 2018/06/04
 */
@Data
@ApiModel(value = "用户账号登录请求对象")
public class BackendUserLoginReqDTO implements Serializable {
    @JsonProperty("username")
    @ApiModelProperty(value = "用户名", example = "AdminUser")
    @NotBlank(field = "用户名", message = "用户名为空")
    private String username;

    @JsonProperty("password")
    @ApiModelProperty(value = "密码", example = "123456")
    @NotBlank(field = "密码", message = "密码为空")
    private String password;
}
