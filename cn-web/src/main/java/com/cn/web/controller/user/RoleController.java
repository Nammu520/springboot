package com.cn.web.controller.user;

import com.cn.base.annotation.SnakeNaming;
import com.cn.base.dto.user.BackendRoleReqDto;
import com.cn.base.resp.RespData;
import com.cn.base.vo.user.MenuTempleVO;
import com.cn.base.vo.user.RoleMenuTreeVO;
import com.cn.base.vo.user.RoleVO;
import com.cn.service.user.RoleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色管理
 *
 * @author dengyu
 * @Date 2019/4/19
 */
@RestController
@RequestMapping("/role")
@Api(tags = "角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @DeleteMapping("/delete/{id}/")
    public RespData<List<MenuTempleVO>> deleteRole(@PathVariable("id") Integer id) {
        roleService.deleteRole(id);
        return RespData.getInstance().success();
    }

    @ApiOperation(value = "获取角色列表", notes = "获取角色列表")
    @GetMapping("/list/")
    public RespData<PageInfo<RoleVO>> list(@Valid @SnakeNaming BackendRoleReqDto param) {
        PageInfo<RoleVO> pageInfo = roleService.list(param);
        return RespData.getInstance().success(pageInfo);
    }

    @ApiOperation(value = "获取角色详情", notes = "获取角色详情")
    @GetMapping("/role_detail/{id}/")
    public RespData<RoleMenuTreeVO> getRoleDetail(@PathVariable("id") Integer id) {
        return RespData.getInstance().success(roleService.getRoleDetail(id));
    }
}
