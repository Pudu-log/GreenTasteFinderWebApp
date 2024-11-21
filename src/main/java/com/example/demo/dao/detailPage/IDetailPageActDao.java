package com.example.demo.dao.detailPage;

import com.example.demo.dto.Act.ActDto;
import com.example.demo.dto.Act.TotalActDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IDetailPageActDao {
    int likeAllSelect(@Param("act") ActDto act);
    ActDto selectAct(@Param("act") ActDto act);
    TotalActDto selectTotalAct(@Param("act") ActDto act);
    int insertAct(@Param("act") ActDto act);
    int deleteAct(@Param("act") ActDto act);
}
