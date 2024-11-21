package com.example.demo.service;

import com.example.demo.dao.member.IMemberDao;
import com.example.demo.dao.myPage.IMyPageActDao;
import com.example.demo.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class MyPageService {
    @Autowired
    IMyPageActDao myPageActDao;

    @Autowired
    IMemberDao memberDao;

    // tbl_user 업데이트
    public int memberUpdate(MemberDto memberDto) {
        return memberDao.memberUpdate(memberDto);
    }

    // 아이디로 멤버 가져오기
    public MemberDto getMemberById(String id) {
        return memberDao.getMemberById(id);
    }

    public List<String> getActStoreList(String gubn, String id) {
        return myPageActDao.getActStoreList(gubn, id);
//        if (val.equals("B")) {
//            return myPageActDao.getFavorStoreList(id);
//        } else if (val.equals("G")) {
//            return myPageActDao.getGoodStoreList(id);
//        }
//        return null;
    }
}
