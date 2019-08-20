package com.cn.service.user.impl;

import com.cn.base.config.LocaleMessageSourceService;
import com.cn.base.exception.SysException;
import com.cn.base.resp.ReturnCodeEnum;
import com.cn.base.vo.user.MenuTempleVO;
import com.cn.persist.user.dao.BackendMenuMapper;
import com.cn.persist.user.model.BackendMenu;
import com.cn.service.user.MenuService;
import com.cn.service.util.FunctionTreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class MenuServiceImpl implements MenuService {

    @Autowired
    private BackendMenuMapper menuMapper;

    @Autowired
    private LocaleMessageSourceService messageSourceService;

    @Override
    public List<MenuTempleVO> getMenuTemple() {
        // 获取所有菜单
        List<BackendMenu> menus = menuMapper.selectListByParams(new HashMap<>());
        if(CollectionUtils.isEmpty(menus)) {
            log.error("获取菜单模板为空");
            throw new SysException(ReturnCodeEnum.ERROR_GET_MEUN_LIST, messageSourceService.getMessage(ReturnCodeEnum.ERROR_GET_MEUN_LIST));
        }
        List<MenuTempleVO> menuTempleVOS = FunctionTreeUtil.buildFunctionTree(menus);
        FunctionTreeUtil.sortTree(menuTempleVOS);
        return menuTempleVOS;
    }
}
