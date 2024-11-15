package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyPageController {

    @RequestMapping("/myPage")
    public String myPage() {
        return "myPage";
    }

    @RequestMapping("/myPage-userInfo")
    public String myPageUserInfo() {
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
