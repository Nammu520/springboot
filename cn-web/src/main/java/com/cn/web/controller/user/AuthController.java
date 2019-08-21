package com.cn.web.controller.user;

import com.cn.base.dto.resp.RespData;
import com.cn.service.user.AuthService;
import com.cn.web.controller.BaseController;
import com.cn.base.dto.user.BackendUserLoginReqDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户鉴权
 *
 * @author dengyu
 * @Date 2019/4/19
 */
@RestController
@RequestMapping("/auth")
@Api(tags = "用户鉴权")
@Validated
public class AuthController extends BaseController {

    @Autowired
    AuthService authService;

    /**
     * 登出
     *
     * @return RespData
     */
    @ApiOperation(value = "登出", notes = "登出")
    @PostMapping("/logout/")
    public RespData logout() {
        String ticket = getTicket();
        authService.logout(ticket);
        return RespData.getInstance().success();
    }

    /**
     * 登录
     *
     * @return RespData
     */
    @ApiOperation(value = "登录", notes = "登出")
    @PostMapping("/login/")
    public RespData login(@RequestBody @Valid BackendUserLoginReqDTO userLoginReqDto) {
        // 生成ticket
        authService.login(userLoginReqDto);
        return RespData.getInstance().success();
    }
}
