package com.example.demo.service;

import com.example.demo.dao.admin.IAdminDao;
import com.example.demo.dao.member.IMemberDao;
import com.example.demo.dto.MemberDto;
import com.example.demo.dto.RoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final IMemberDao memberDao;
    private final IAdminDao adminDao;


    public List<MemberDto> memberList(int page) {
        int offset = (page - 1) * 10;
        return memberDao.memberList(offset);
    }

    public int deleteMember(String id) {
        return memberDao.deleteMember(id);
    }

    public int insertRoom(RoomDto roomDto) {
        return adminDao.insertRoom(roomDto);
    }

    public List<RoomDto> roomList() {
        return adminDao.roomList();
    }

    public int deleteRoom(String roomCode) {
        return adminDao.deleteRoom(roomCode);
    }

}
