package com.example.demo.dao.mainpage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/* 
작성자: 구경림
작성일: 2024.11.20
View를 렌더링하기 위한 컨트롤러로, 사용자 세션과 클라이언트 요청을 기반으로 데이터를 가공하여 JSP로 전달.
음식점 데이터 페이징 및 정렬 처리로 클라이언트에서 효율적인 데이터를 제공.
사용자 세션이 유효하지 않으면 로그인 페이지로 리다이렉트하여 보안을 강화.
*/
@Mapper
public interface IMainPageDao {

    // 좋아요/즐겨찾기 추가(임시)
    int insertAct(@Param("memberId") String id, @Param("storeId") String storeId, @Param("gubn") String gubn);

    // 좋아요/즐겨찾기 삭제
    int deleteAct(@Param("memberId") String id, @Param("storeId") String storeId, @Param("gubn") String gubn);

    // 좋아요/즐겨찾기 여부 확인
    List<String> findUserActions(@Param("memberId") String id, @Param("gubn") String gubn);
}
