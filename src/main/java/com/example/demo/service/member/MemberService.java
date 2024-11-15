package com.example.demo.service.member;

import com.example.demo.dao.member.IMemberDao;
import com.example.demo.dto.MemberDto;
import com.example.demo.dto.RoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final IMemberDao memberDao;

    @Autowired
    public MemberService(IMemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public int memberInsert(MemberDto member) {
        return memberDao.memberInsert(member);
    }

    public List<RoomDto> getSelectBox() {
        return memberDao.selectBox();
    }

    public int idCheck(String id) {
        return memberDao.idCheck(id);
    }

    public MemberDto login(String id, String password){
        return memberDao.login(id,password);
    }

}
