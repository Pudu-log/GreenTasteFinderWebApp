package com.example.demo.controller.mainPage;

import com.example.demo.model.Restaurant;
import com.example.demo.service.MainPageService;
import com.example.demo.utils.PagingBtn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainPageController {

    private final MainPageService mainPageService;

    @Autowired
    public MainPageController(MainPageService mainPageService) {
        this.mainPageService = mainPageService;
    }

    @GetMapping("/")
    public String getRestaurants(Model model) {
        return getRestaurantsByPage(1, model);
    }

    @GetMapping("/page/{pageNumber}")
    public String getRestaurantsByPage(@PathVariable("pageNumber") int pageNumber, Model model) {
        int size = 12;

        List<Restaurant> allRestaurants = mainPageService.getNearbyRestaurants();

        int totalRestaurants = allRestaurants.size();
        PagingBtn pagingBtn = new PagingBtn(totalRestaurants, pageNumber);
        int start = (pageNumber - 1) * size;
        int end = Math.min(start + size, totalRestaurants);
        List<Restaurant> restaurants = allRestaurants.subList(start, end);

        model.addAttribute("restaurants", restaurants);
        model.addAttribute("pagingBtn", pagingBtn);
        model.addAttribute("currentPage", pageNumber);

        return "mainPage";
    }


}
