package com.example.demo.dao.admin;

import com.example.demo.dto.RoomDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IAdminDao {

    List<RoomDto> roomList();

    int insertRoom(@Param("room") RoomDto roomDto);

    int deleteRoom(String roomCode);
}
