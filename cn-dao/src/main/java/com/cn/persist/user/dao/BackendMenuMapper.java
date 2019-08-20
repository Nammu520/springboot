package com.cn.persist.user.dao;

import com.cn.persist.user.model.BackendMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BackendMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BackendMenu record);

    int insertSelective(BackendMenu record);

    BackendMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BackendMenu record);

    int updateByPrimaryKey(BackendMenu record);

    List<BackendMenu> selectListByParams(Map<String, Object> params);
}