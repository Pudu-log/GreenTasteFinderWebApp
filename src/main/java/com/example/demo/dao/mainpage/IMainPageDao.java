package com.example.demo.dao.mainpage;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMainPageDao {

    // 좋아요/즐겨찾기 추가(임시)
    int insertAct(@Param("memberId") String id, @Param("storeId") String storeId, @Param("gubn") String gubn);
}
