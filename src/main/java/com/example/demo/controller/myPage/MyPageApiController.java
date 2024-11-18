package com.example.demo.controller.myPage;

import com.example.demo.dto.MemberDto;
import com.example.demo.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class MyPageApiController {
    private final MyPageService myPageService;

    @Autowired
    public MyPageApiController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @PostMapping("/updateMember")
    int update(@RequestBody MemberDto memberDto) {
        return myPageService.update(memberDto);
    }
}
