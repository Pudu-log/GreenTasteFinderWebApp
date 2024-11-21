package com.example.demo.controller.mainPage;

import com.example.demo.dto.MemberDto;
import com.example.demo.model.Restaurant;
import com.example.demo.service.MainPageService;
import com.example.demo.utils.PagingBtn;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/*
작성자: 구경림  
작성일: 2024.11.20  
작성이유:  
1. View와 데이터를 연결하여 사용자 인터페이스에 필요한 정보를 제공.  
2. 사용자 요청에 따라 음식점 데이터를 필터링하고 정렬하여 화면에 렌더링.  

리팩토링 포인트:  
- `sortRestaurants` 메서드가 API 컨트롤러와 중복됨. 공통 유틸로 추출 필요.  
- 페이징 로직이 비즈니스 로직과 섞여 있음. 서비스 계층으로 이동 필요.  
- 세션 검증 로직을 별도 유틸 클래스로 추출하여 재사용성 높이기.  
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
	                                 @RequestParam(name = "sortBy", defaultValue = "rating") String sortBy,
	                                 Model model, HttpServletRequest request) {

	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        MemberDto member = (MemberDto) session.getAttribute("member");
	        if (member != null && member.getId() != null) {
	            // 기본 검색어 설정
	            if (keyword == null || keyword.isBlank()) {
	                keyword = "restaurant";
	            }

	            // 데이터 가져오기
	            List<Restaurant> allRestaurants = mainPageService.fetchInitialRestaurants(keyword);

	            // 정렬 기준에 맞춰 데이터 정렬
	            sortRestaurants(allRestaurants, sortBy);

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
	            model.addAttribute("sortBy", sortBy);  // 선택된 정렬 기준 전달

	            return "mainPage";
	        }
	    }
	    if (session != null) {
	        session.invalidate();
	    }
	    return "redirect:/login";
	}

	/**
	 * 레스토랑 리스트를 정렬합니다.
	 * @param restaurants 음식점 리스트
	 * @param sortBy 정렬 기준
	 */
	private void sortRestaurants(List<Restaurant> restaurants, String sortBy) {
	    switch (sortBy) {
	        case "rating":
	            restaurants.sort((r1, r2) -> Double.compare(r2.getRating(), r1.getRating()));
	            break;
	        case "reviewCount":
	            restaurants.sort((r1, r2) -> Integer.compare(r2.getReviewCount(), r1.getReviewCount()));
	            break;
	        case "distance":
	            restaurants.sort((r1, r2) -> Double.compare(r1.getDistance(), r2.getDistance()));
	            break;
	        default:
	            restaurants.sort((r1, r2) -> Double.compare(r2.getRating(), r1.getRating()));
	            break;
	    }
	}

}
