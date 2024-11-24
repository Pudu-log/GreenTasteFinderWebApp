package com.example.demo.controller.mainPage;

import com.example.demo.dto.MemberDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Restaurant;
import com.example.demo.service.MainPageService;
import com.example.demo.utils.PagingBtn;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/*
작성자: 구경림  
작성일: 2024.11.20  
*/

@Controller
@RequestMapping("/")
public class MainPageController {

    private final MainPageService mainPageService;

    public MainPageController(MainPageService mainPageService) {
        this.mainPageService = mainPageService;
    }

    @GetMapping
    public String getRestaurantsPage(@RequestParam(name = "keyword", required = false) String keyword,
                                     @RequestParam(name = "page", defaultValue = "1") int currentPage,
                                     @RequestParam(name = "sortBy", defaultValue = "distance") String sortBy,
                                     Model model, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            MemberDto member = (MemberDto) session.getAttribute("member");
            if (member != null && member.getId() != null) {
            	model.addAttribute("loginedState","loginedState");
            	model.addAttribute("memberId",member.getId());
            	System.out.println("member" + member.getId());
            }
        }
        // 기본 검색어 설정
        if (keyword == null || keyword.isBlank()) {
        	keyword = "restaurant";
        }
        
        List<Restaurant> allRestaurants = mainPageService.fetchInitialRestaurants(keyword);
        
        mainPageService.sortRestaurants(allRestaurants, sortBy);
        
        // 페이징 처리
        int totalCount = allRestaurants.size();
        PagingBtn pagingBtn = new PagingBtn(totalCount, currentPage);
        
        int startIndex = (currentPage - 1) * pagingBtn.getPageSize();
        int endIndex = Math.min(startIndex + pagingBtn.getPageSize(), totalCount);
        List<Restaurant> paginatedRestaurants = allRestaurants.subList(startIndex, endIndex);
        
        // 모델에 데이터 추가
        model.addAttribute("restaurants", paginatedRestaurants);
        model.addAttribute("pagingBtn", pagingBtn);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);  // 선택된 정렬 기준 전달
        return "mainPage";
    }
    
    @GetMapping("/mainPage/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/"; 
    }

}
