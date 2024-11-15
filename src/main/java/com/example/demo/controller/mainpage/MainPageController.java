package com.example.demo.controller.mainpage;

import org.springframework.stereotype.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Restaurant;
import com.example.demo.service.MainPageService;


@Controller
@RequestMapping("/mainpage")
public class MainPageController {
    private final MainPageService mainPageService;

    @Autowired
    public MainPageController(MainPageService mainPageService) {
        this.mainPageService = mainPageService;
    }

    @GetMapping
    public String getRestaurants(Model model) {
    	
        // 고정된 좌표 설정
        double fixedLatitude = 35.159707;
        double fixedLongitude = 129.060186;

        List<Restaurant> restaurants = mainPageService.getNearbyRestaurants(fixedLatitude, fixedLongitude);

        model.addAttribute("restaurants", restaurants);

        return "mainpage"; 
    }

}
