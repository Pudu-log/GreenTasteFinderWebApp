package com.example.demo.controller.mainPage;

import com.example.demo.model.Restaurant;
import com.example.demo.service.MainPageService;
import com.example.demo.utils.GoogleNearByPlaceApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
/*
작성자: 구경림  
작성일: 2024.11.20  
*/

@RestController
@RequestMapping("/api/restaurants")
public class MainPageApiController {

	private final MainPageService mainPageService;

	public MainPageApiController(MainPageService mainPageService) {
		this.mainPageService = mainPageService;
	}

	@GetMapping("/all")
	public CompletableFuture<ResponseEntity<List<Restaurant>>> getAllRestaurants(
			@RequestParam(name = "keyword", defaultValue = "restaurant") String keyword,
			@RequestParam(name = "memberId", required = false) String memberId,
			@RequestParam(name = "sortBy", defaultValue = "rating") String sortBy) {

		return mainPageService.fetchAllRestaurants(keyword).thenApply(restaurants -> {
			// 사용자의 좋아요/즐겨찾기 정보를 업데이트
			if (memberId != null && !memberId.isEmpty()) {
				List<String> likedStores = mainPageService.getUserActions(memberId, "G");
				List<String> favoritedStores = mainPageService.getUserActions(memberId, "F");

				for (Restaurant restaurant : restaurants) {
					restaurant.setLiked(likedStores.contains(restaurant.getPlaceId()));
					restaurant.setFavorited(favoritedStores.contains(restaurant.getPlaceId()));
				}
			}

			mainPageService.sortRestaurants(restaurants, sortBy);

			return ResponseEntity.ok(restaurants);
		});
	}

	@PostMapping("/toggle")
	public ResponseEntity<Void> toggleAction(@RequestParam(name = "memberId") String memberId,
			@RequestParam(name = "storeId") String storeId, @RequestParam(name = "gubn") String gubn) {

		List<String> actions = mainPageService.getUserActions(memberId, gubn);
		System.out.println("Member ID: " + memberId);
		System.out.println("Store ID: " + storeId);
		System.out.println("Gubn: " + gubn);

		if (actions.contains(storeId)) {
			mainPageService.removeUserAction(memberId, storeId, gubn);
		} else {
			mainPageService.addUserAction(memberId, storeId, gubn);
		}

		return ResponseEntity.ok().build();
	}

	@GetMapping("/status")
	public ResponseEntity<Map<String, List<String>>> getUserActions(@RequestParam(name = "memberId") String memberId) {
		List<String> likedStores = mainPageService.getUserActions(memberId, "G");
		List<String> favoritedStores = mainPageService.getUserActions(memberId, "F");

		Map<String, List<String>> status = new HashMap<>();
		status.put("likedStores", likedStores);
		status.put("favoritedStores", favoritedStores);

		return ResponseEntity.ok(status);
	}

}
