package com.example.demo.controller.mainPage;

import org.springframework.stereotype.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Restaurant;
import com.example.demo.service.MainPageService;

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

	// 페이지 번호를 경로 변수로 받는 메서드
	@GetMapping("/page/{pageNumber}")
	public String getRestaurantsByPage(@PathVariable("pageNumber") int pageNumber, Model model) {
		int size = 12; // 페이지당 표시할 항목 수 (고정값)

		List<Restaurant> allRestaurants = mainPageService.getNearbyRestaurants();

		// 총 페이지 수 계산
		int totalRestaurants = allRestaurants.size();
		int totalPages = (int) Math.ceil((double) totalRestaurants / size);

		// 페이지 번호 범위 체크
		if (pageNumber < 1) {
			pageNumber = 1;
		} else if (pageNumber > totalPages) {
			pageNumber = totalPages;
		}

		// 현재 페이지에 표시할 레스토랑 목록
		int start = (pageNumber - 1) * size;
		int end = Math.min(start + size, totalRestaurants);
		List<Restaurant> restaurants = allRestaurants.subList(start, end);

		// 페이지네이션 범위 계산
		int pageGroupSize = 5; // 한 번에 표시할 페이지 번호 수
		int startPage = ((pageNumber - 1) / pageGroupSize) * pageGroupSize + 1;
		int endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

		model.addAttribute("restaurants", restaurants);
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		// API 요청 수 출력
		int apiRequestCount = mainPageService.getApiRequestCount();
		System.out.println("총 API 요청 cnt : " + apiRequestCount);
		System.out.println("로딩된 식당 수 .... " + restaurants.size());

		return "mainPage";
	}

	@GetMapping("/jsondebuging")
	public @ResponseBody List<Restaurant> getNearbyRestaurants() {
		List<Restaurant> restaurants = mainPageService.getNearbyRestaurants();
		return restaurants;
	}
}
