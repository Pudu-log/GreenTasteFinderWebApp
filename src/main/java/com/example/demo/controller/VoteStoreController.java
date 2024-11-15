package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VoteStoreController {

    @RequestMapping("/voteStore")
    public String voteSotre() {
        return "voteStore";
    }

}
