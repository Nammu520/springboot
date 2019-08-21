package com.cn.service.util;

import com.cn.base.vo.user.MenuTempleVO;
import com.cn.persist.user.model.BackendMenu;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     *
     * 遍历所有节点,如果某个节点的child都未勾选,则将该子节点置为未勾选
     *                  某个节点的child只要有一个勾选,那当前节点置为勾选
     *
     * @param resources 节点
     *
     * @return
     */
    public static void checkNode(List<MenuTempleVO> resources) {
        resources.stream().forEach(resource -> {
            if(resource.getCheckFlag()) {
                if(!checkChildNotCheck(resource)){
                    resource.setCheckFlag(false);
                }
            } else{ //节点未勾选
                if(checkChildNotCheck(resource)) {
                    resource.setCheckFlag(true);
                }
            }
        });
    }

    /**
     *
     * 判断某一个子节点下所有节点都未勾选
     *
     * @param resoure
     *
     * @return
     */
    private static boolean checkChildNotCheck(MenuTempleVO resoure){
        if(CollectionUtils.isEmpty(resoure.getChildren())){
            return false;
        }else{
            for(MenuTempleVO i : resoure.getChildren()){
                if(i.getCheckFlag()){
                    return true;
                }
                if(checkChildNotCheck(i)){
                    return true;
                }
            }
            return false;
        }
    }

    /**
     *
     * 遍历权限树(获取所有勾选的权限Id)
     *
     * @param resoures 权限树中的某一个节点
     * @param menuIds 所有勾选的权限Id集合
     *
     * @return
     */
    public static void dfsFunctionTree(List<MenuTempleVO> resoures,Set<Integer> menuIds){
        resoures.stream().forEach(resoure -> {
            if(resoure.getCheckFlag()){
                menuIds.add(resoure.getId());
            }
            dfsFunctionTree(resoure.getChildren(), menuIds);
        });
    }
}
