package com.cn.web.controller.user;

import com.cn.base.dto.resp.RespData;
import com.cn.base.vo.user.RoleMenuTreeVO;
import com.cn.service.user.UserService;
import com.cn.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理
 *
 * @author dengyu
 * @Date 2019/4/19
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取当前用户权限", notes = "获取当前用户权限")
    @GetMapping("/get_user_permissions")
    public RespData<RoleMenuTreeVO> getUserPermissions() {
        RoleMenuTreeVO roleMenuTreeVO = userService.getUserPermissions(getUserId());
        return RespData.getInstance().success(roleMenuTreeVO);
    }

}
