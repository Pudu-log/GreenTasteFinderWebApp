package com.example.demo.controller.mainPage;

import com.example.demo.model.Restaurant;
import com.example.demo.service.MainPageService;
import com.example.demo.utils.GoogleNearByPlaceApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/restaurants")
public class MainPageApiController {

    private final MainPageService mainPageService;

    public MainPageApiController(MainPageService mainPageService) {
        this.mainPageService = mainPageService;
    }

    // JSON으로 초기 데이터 반환
    @GetMapping
    public ResponseEntity<List<Restaurant>> getInitialRestaurants(@RequestParam String keyword) {
        List<Restaurant> restaurants = mainPageService.fetchInitialRestaurants(keyword);
        return ResponseEntity.ok(restaurants);
    }

    // 모든 데이터를 비동기로 반환
    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<Restaurant>>> getAllRestaurants(
            @RequestParam(name = "keyword", defaultValue = "restaurant") String keyword) {
        return mainPageService.fetchAllRestaurants(keyword)
                .thenApply(ResponseEntity::ok);
    }
}

