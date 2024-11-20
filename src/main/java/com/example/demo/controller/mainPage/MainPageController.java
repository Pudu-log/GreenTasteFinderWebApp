package com.example.demo.controller.mainPage;

import com.example.demo.model.Restaurant;
import com.example.demo.service.MainPageService;
import com.example.demo.utils.PagingBtn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class MainPageController {

    private final MainPageService mainPageService;

    public MainPageController(MainPageService mainPageService) {
        this.mainPageService = mainPageService;
    }

    @GetMapping
    public String getRestaurantsPage(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "1") int currentPage,
            Model model) {

        // 기본 검색어 설정
        if (keyword == null || keyword.isBlank()) {
            keyword = "restaurant";
        }

        // 데이터 가져오기
        List<Restaurant> allRestaurants = mainPageService.fetchInitialRestaurants(keyword);

        // 페이징 처리
        int totalCount = allRestaurants.size();
        PagingBtn pagingBtn = new PagingBtn(totalCount, currentPage);

        // 현재 페이지에 해당하는 데이터 추출
        int startIndex = (currentPage - 1) * pagingBtn.getPageSize();
        int endIndex = Math.min(startIndex + pagingBtn.getPageSize(), totalCount);
        List<Restaurant> paginatedRestaurants = allRestaurants.subList(startIndex, endIndex);

        // 모델에 데이터 추가
        model.addAttribute("restaurants", paginatedRestaurants);
        model.addAttribute("pagingBtn", pagingBtn);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("keyword", keyword);

        return "mainPage";
    }
}
