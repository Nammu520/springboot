package com.cn.service.user;


import com.cn.base.dto.user.BackendRoleReqDto;
import com.cn.base.vo.user.RoleMenuTreeVO;
import com.cn.base.vo.user.RoleVO;
import com.github.pagehelper.PageInfo;

public interface RoleService {

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    void deleteRole(Integer id);

    /**
     * 获取角色列表
     *
     * @param param 查询参数
     */
    PageInfo<RoleVO> list(BackendRoleReqDto param);

    /**
     * 角色详情
     *
     * @param id 角色id
     */
    RoleMenuTreeVO getRoleDetail(Integer id);
}
