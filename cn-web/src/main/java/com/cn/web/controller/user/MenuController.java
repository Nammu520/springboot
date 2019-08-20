package com.cn.web.controller.user;

import com.cn.base.dto.resp.RespData;
import com.cn.base.vo.user.MenuTempleVO;
import com.cn.service.user.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单管理
 *
 * @author dengyu
 * @Date 2019/4/19
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "菜单管理")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "获取菜单模板", notes = "获取菜单模板")
    @GetMapping("/getMeuns/")
    public RespData<List<MenuTempleVO>> getAuthTemple() {
        List<MenuTempleVO> menuTempleVOS = this.menuService.getMenuTemple();
        return RespData.getInstance().success(menuTempleVOS);
    }
}
