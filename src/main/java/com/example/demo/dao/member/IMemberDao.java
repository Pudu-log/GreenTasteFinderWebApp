package com.example.demo.dao.member;

import com.example.demo.dto.MemberDto;
import com.example.demo.dto.RoomDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMemberDao {
    int memberInsert(@Param("member") MemberDto memberDto);

    int idCheck(String id);

    MemberDto login(@Param("id") String id, @Param("pw") String pw);

    List<RoomDto> selectBox();

    int update(@Param("member") MemberDto memberDto);

    int deleteMember(String id);

    MemberDto getMemberById(@Param("id") String id);

    List<MemberDto> memberList(@Param("offset") int offset);

    int getCount();



}
