package com.example.demo.controller.myPage;

import com.example.demo.dto.MemberDto;
import com.example.demo.service.MyPageService;
import com.example.demo.utils.GooglePlaceApi;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MyPageController {

    private final MyPageService myPageService;

    @Autowired
    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @RequestMapping("/myPage")

    public String myPage(HttpSession session) {
        return checkNull(session.getAttribute("member")) ? "redirect:/login" : "myPage";
    }

    @RequestMapping("/myPage-userInfo")
    public String myPageUserInfo(HttpSession session, Model model) {
        MemberDto member = (MemberDto) session.getAttribute("member");
        if (checkNull(member)) {
            return "redirect:/login";
        } else {
            String id = member.getId();
            model.addAttribute("member", myPageService.getMemberById(id));
            return "myPage_userInfo";
        }

    }

    @RequestMapping("/myPage-act")
    public String myPageAct(HttpSession session, @RequestParam("gubn") String gubn, Model model) {
        MemberDto member = (MemberDto) session.getAttribute("member");
        if (checkNull(member)) {
            return "redirect:/login";
        }
        model.addAttribute("gubn", gubn);
        return "myPage_act";
    }

    private boolean checkNull(Object object) {
        return object == null;
    }

}
