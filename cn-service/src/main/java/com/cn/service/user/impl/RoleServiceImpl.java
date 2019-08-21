package com.cn.service.user.impl;

import com.cn.base.dto.user.BackendRoleReqDTO;
import com.cn.base.dto.user.SaveRoleReqDTO;
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
import com.cn.persist.user.model.BackendRoleMenuKey;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    public PageData<RoleVO> list(BackendRoleReqDTO param){
       Map<String, Object> sqlParams = new HashMap<>();
       sqlParams.put("nameLike", param.getNameLike());
       Page page = PageHelper.startPage(param.getPageNum(), param.getPageSize());
       List<BackendRole> roles =  roleMapper.selectByParams(sqlParams);
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

    @Transactional
    @Override
    public void saveRole(SaveRoleReqDTO param){
        /**校验角色名是否重复**/
        Map<String, Object> sqlParams = new HashMap<>();
        sqlParams.put("name", param.getName());
        List<BackendRole> roles =  roleMapper.selectByParams(sqlParams);
        if(CollectionUtils.isNotEmpty(
                roles.stream().filter(role-> !role.getId().equals(param.getId())).collect(Collectors.toList()))) {
            throw new SysException(ReturnCodeEnum.ERROR_ROLE_NAME_REPEAT);
        }
        // 组装数据
        FunctionTreeUtil.checkNode(param.getMenuTree());
        // 获取菜单id
        Set<Integer> menuIds = new HashSet<>();
        FunctionTreeUtil.dfsFunctionTree(param.getMenuTree(), menuIds);
        // 判断新增还是修改
        BackendRole role = new BackendRole();
        role.setName(param.getName());
        if(param.getId() != null) { //修改
            roleMenuMapper.deleteByRoleId(param.getId());
            // 修改角色名
            role.setId(param.getId());
            roleMapper.updateByPrimaryKeySelective(role);
        } else{
            // 保存角色信息
            roleMapper.insertSelective(role);
        }
        // 批量新增角色菜单管理关系
        if(CollectionUtils.isNotEmpty(menuIds)) {
            List<BackendRoleMenuKey> insertList = new ArrayList<>();
            menuIds.stream().forEach(menuId -> insertList.add(new BackendRoleMenuKey(role.getId(), menuId)));
            roleMenuMapper.insertBatch(insertList);
        }
    }
}
