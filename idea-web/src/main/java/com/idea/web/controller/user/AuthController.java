package com.idea.web.controller.user;

import com.idea.base.resp.RespData;
import com.idea.service.AuthService;
import com.idea.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        authService.logout(getUserId(), ticket);
        return RespData.getInstance().success();
    }

    /**
     * 登录
     *
     * @return RespData
     */
    @ApiOperation(value = "登出", notes = "登出")
    @PostMapping("/login/")
    public RespData login() {
        authService.login(getUserId());
        return RespData.getInstance().success();
    }
}
