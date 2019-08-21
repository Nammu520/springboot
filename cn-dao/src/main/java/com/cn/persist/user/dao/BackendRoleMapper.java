package com.cn.persist.user.dao;

import com.cn.persist.user.model.BackendRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BackendRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BackendRole record);

    int insertSelective(BackendRole record);

    BackendRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BackendRole record);

    int updateByPrimaryKey(BackendRole record);

    List<BackendRole> selectByParams(Map<String, Object> params);
}