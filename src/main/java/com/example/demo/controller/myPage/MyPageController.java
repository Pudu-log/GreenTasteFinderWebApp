package com.example.demo.controller.myPage;

import com.example.demo.service.MyPageService;
import com.example.demo.utils.GooglePlaceApi;
import com.fasterxml.jackson.databind.JsonNode;
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

    public String myPage() {
        return "myPage";
    }

    @RequestMapping("/myPage-userInfo")
    public String myPageUserInfo(@RequestParam("id") String id, Model model) {
        model.addAttribute("member", myPageService.getMemberById(id));
        return "myPage_userInfo";
    }

    @RequestMapping("/myPage-favor")
    public String myPageFavor() {
        return "myPage_favor";
    }

    @RequestMapping("/myPage-visit")
    public String myPageVisit() {
        return "myPage_visit";
    }

    @RequestMapping("/myPage-good")
    public String myPageGood() {
        return "myPage_good";
    }

}
