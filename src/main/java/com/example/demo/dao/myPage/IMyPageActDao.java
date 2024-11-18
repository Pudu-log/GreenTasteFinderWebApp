package com.example.demo.dao.myPage;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMyPageActDao {
    List<String> getActStoreList(@Param("gubn") String gubn, @Param("id") String id);
}
