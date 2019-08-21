package com.cn.persist.user.dao;

import com.cn.persist.user.model.BackendUserRole;
import com.cn.persist.user.model.BackendUserRoleKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BackendUserRoleMapper {
    int deleteByPrimaryKey(BackendUserRoleKey key);

    int insert(BackendUserRole record);

    int insertSelective(BackendUserRole record);

    BackendUserRole selectByPrimaryKey(BackendUserRoleKey key);

    int updateByPrimaryKeySelective(BackendUserRole record);

    int updateByPrimaryKey(BackendUserRole record);

    List<BackendUserRole> selectByRoleId(@Param("roleId") Integer roleId);

    BackendUserRole selectByUserId(@Param("userId") Integer userId);
}