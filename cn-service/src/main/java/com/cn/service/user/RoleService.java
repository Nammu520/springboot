package com.cn.service.user;


import com.cn.base.dto.user.BackendRoleReqDTO;
import com.cn.base.dto.resp.PageData;
import com.cn.base.dto.user.SaveRoleReqDTO;
import com.cn.base.vo.user.RoleMenuTreeVO;
import com.cn.base.vo.user.RoleVO;

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
    PageData<RoleVO> list(BackendRoleReqDTO param);

    /**
     * 角色详情
     *
     * @param id 角色id
     */
    RoleMenuTreeVO getRoleDetail(Integer id);

    /**
     * 保存角色
     *
     * @param param 角色信息
     */
    void saveRole(SaveRoleReqDTO param);
}
