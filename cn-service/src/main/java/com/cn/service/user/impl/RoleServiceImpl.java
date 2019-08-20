package com.cn.service.user.impl;

import com.cn.base.dto.user.BackendRoleReqDto;
import com.cn.base.exception.SysException;
import com.cn.base.dto.resp.PageData;
import com.cn.base.enums.ReturnCodeEnum;
import com.cn.base.vo.user.MenuTempleVO;
import com.cn.base.vo.user.RoleMenuTreeVO;
import com.cn.base.vo.user.RoleVO;
import com.cn.persist.user.dao.BackendMenuMapper;
import com.cn.persist.user.dao.BackendRoleMapper;
import com.cn.persist.user.dao.BackendRoleMenuMapper;
import com.cn.persist.user.dao.BackendUserRoleMapper;
import com.cn.persist.user.model.BackendMenu;
import com.cn.persist.user.model.BackendRole;
import com.cn.persist.user.model.BackendRoleMenu;
import com.cn.persist.user.model.BackendUserRole;
import com.cn.service.user.RoleService;
import com.cn.service.util.FunctionTreeUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RoleServiceImpl implements RoleService {

    @Autowired
    private BackendRoleMapper roleMapper;

    @Autowired
    private BackendUserRoleMapper userRoleMapper;

    @Autowired
    private BackendRoleMenuMapper roleMenuMapper;

    @Autowired
    private BackendMenuMapper menuMapper;

    @Transactional
    @Override
    public void deleteRole(Integer id) {
        BackendRole backendRole = roleMapper.selectByPrimaryKey(id);
        if(backendRole == null) {
            throw new SysException(ReturnCodeEnum.ERROR_ROLE_NOT_EXIST);
        }
        // 获取改角色下绑定的用户
        List<BackendUserRole> userRoles = userRoleMapper.selectByRoleId(id);
        if(!CollectionUtils.isEmpty(userRoles)) {
            throw new SysException(ReturnCodeEnum.ERROR_ROLE_BIND_USER);
        }
        // 删除角色
        roleMapper.deleteByPrimaryKey(id);
        // 删除角色关联菜单表
        roleMenuMapper.deleteByRoleId(id);
    }

    @Override
    public PageData<RoleVO> list(BackendRoleReqDto param){
       Page page = PageHelper.startPage(param.getPageNum(), param.getPageSize());
       List<BackendRole> roles =  roleMapper.selectByParams(param.getName());
       List<RoleVO> roleVOS = new ArrayList<>();
       roles.stream().forEach(role -> {
           RoleVO roleVo = new RoleVO();
           BeanUtils.copyProperties(role, roleVo);
           roleVOS.add(roleVo);
       });
        PageData<RoleVO> pageData = new PageData<>();
        pageData.setCount(page.getTotal());
        pageData.setContent(roleVOS);
        return pageData;
    }

    @Override
    public RoleMenuTreeVO getRoleDetail(Integer id) {
        RoleMenuTreeVO result = new RoleMenuTreeVO();
        BackendRole role = roleMapper.selectByPrimaryKey(id);
        if(role == null) {
            throw new SysException(ReturnCodeEnum.ERROR_ROLE_NOT_EXIST);
        }
        BeanUtils.copyProperties(role, result);
        //获取系统所有权限
        List<BackendMenu> menus = menuMapper.selectListByParams(new HashMap<>());
        //获取角色权限
        List<BackendRoleMenu> roleMenus = roleMenuMapper.selectByRoleId(id);
        Set<Integer> menusId = roleMenus.stream().map(BackendRoleMenu::getMenuId).collect(Collectors.toSet());
        List<MenuTempleVO> menuTree = FunctionTreeUtil.buildFunctionTree(menus, menusId);
        result.setMenuTree(menuTree);
        return result;
    }
}
