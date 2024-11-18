package com.example.demo.dao.myPage;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMyPageActDao {
    List<String> getGoodStoreList(@Param("id") String id);
    List<String> getFavorStoreList(@Param("id") String id);
    List<String> getActStoreList(@Param("id") String id, @Param("gubn") String gubn);
}
