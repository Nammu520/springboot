package com.cn.service.util;

import com.cn.base.vo.user.MenuTempleVO;
import com.cn.persist.user.model.BackendMenu;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 构造菜单列表
 */
@Slf4j
public class FunctionTreeUtil {

    /**
     * 构造模板权限树
     *
     */
    public static List<MenuTempleVO> buildFunctionTree(List<BackendMenu> resources){
        return buildFunctionTree(resources, new HashSet<>());
    }

    /**
     * 构造角色权限树
     */
    public static List<MenuTempleVO> buildFunctionTree(List<BackendMenu> resources, Set<Integer> menuIds) {
        List<MenuTempleVO> menuTempleVOS = new ArrayList<>();
        if(!CollectionUtils.isEmpty(resources)) {
            resources.stream().forEach(menu -> {
                if(StringUtils.isBlank(menu.getPCode())) {
                    MenuTempleVO menuTempleVO = copyProperties(menu, menuIds.contains(menu.getId()));
                    buildChildrenFunction(menuTempleVO, resources, menuIds);
                    menuTempleVOS.add(menuTempleVO);
                }
            });
        }
        return menuTempleVOS;
    }

    /**
     * 构造子节点
     */
    public static void buildChildrenFunction(MenuTempleVO target, List<BackendMenu> resources, Set<Integer> menuIds){
        List<MenuTempleVO> childs = new ArrayList<>();
        resources.stream().forEach(menu -> {
            if(menu.getPCode() != null && menu.getPCode().equals(target.getCode())){
                MenuTempleVO menuTempleVO = copyProperties(menu, menuIds.contains(menu.getId()));
                buildChildrenFunction(menuTempleVO, resources, menuIds);
                childs.add(menuTempleVO);
            }
        });
        target.setChildren(childs);
    }

    public static void sortTree(List<MenuTempleVO> resources) {
        if(!CollectionUtils.isEmpty(resources)) {
            Collections.sort(resources, Comparator.comparing(MenuTempleVO::getCode));
            resources.stream().forEach(menuTempleVO -> sortTree(menuTempleVO.getChildren()));
        }
    }

    public static MenuTempleVO copyProperties(BackendMenu resource, Boolean checkFlag){
        MenuTempleVO menuTempleVO = new MenuTempleVO();
        BeanUtils.copyProperties(resource, menuTempleVO);
        menuTempleVO.setCheckFlag(checkFlag);
        return menuTempleVO;
    }
}
