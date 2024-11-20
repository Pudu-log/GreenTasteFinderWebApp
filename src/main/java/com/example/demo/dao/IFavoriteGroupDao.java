package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IFavoriteGroupDao {
    public List<String> getFavoriteGroup(@Param("id") String id, @Param("gubn") String gubn, @Param("limit") int limit, @Param("offset") int offset);
}
