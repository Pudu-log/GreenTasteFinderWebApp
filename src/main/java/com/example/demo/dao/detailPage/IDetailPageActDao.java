package com.example.demo.dao.detailPage;

import com.example.demo.dto.ActDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IDetailPageActDao {
    int likeAllSelect(@Param("act") ActDto act);
    ActDto selectAct(@Param("act") ActDto act);
    int insertAct(@Param("act") ActDto act);
    int deleteAct(@Param("act") ActDto act);
}
