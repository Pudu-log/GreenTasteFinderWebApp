package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GroupFavoriteController {


    @RequestMapping("/group-favorites")
    public String groupFavorite(HttpServletRequest request) {

        return "groupFavorites";
    }
}
