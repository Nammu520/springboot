package com.cn.persist.user.dao;

import com.cn.persist.user.model.BackendRoleMenu;
import com.cn.persist.user.model.BackendRoleMenuKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface BackendRoleMenuMapper {
    int deleteByPrimaryKey(BackendRoleMenuKey key);

    int insert(BackendRoleMenu record);

    int insertSelective(BackendRoleMenu record);

    BackendRoleMenu selectByPrimaryKey(BackendRoleMenuKey key);

    int updateByPrimaryKeySelective(BackendRoleMenu record);

    int updateByPrimaryKey(BackendRoleMenu record);

    int deleteByRoleId(@Param("roleId") Integer roleId);

    List<BackendRoleMenu> selectByRoleId(@Param("roleId") Integer roleId);

    int insertBatch(List<BackendRoleMenuKey> records);
}