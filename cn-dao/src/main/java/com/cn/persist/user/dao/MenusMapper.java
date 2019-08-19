package com.cn.persist.user.dao;

import com.cn.persist.user.model.Menus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenusMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Menus record);

    int insertSelective(Menus record);

    Menus selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Menus record);

    int updateByPrimaryKey(Menus record);
}