package com.example.demo.controller.mainPage;

import com.example.demo.model.Restaurant;
import com.example.demo.service.MainPageService;
import com.example.demo.utils.ApiResponse;
import com.example.demo.utils.PagingBtn;
import com.example.demo.type.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
public class MainPageApiController {

    private final MainPageService mainPageService;

    @Autowired
    public MainPageApiController(MainPageService mainPageService) {
        this.mainPageService = mainPageService;
    }

    @GetMapping("/api/restaurants/{pageNumber}")
    public ApiResponse<List<Restaurant>> getRestaurantsApi(@PathVariable("pageNumber") int pageNumber) {
        int size = 12;

        List<Restaurant> allRestaurants = mainPageService.getNearbyRestaurants();
        int totalRestaurants = allRestaurants.size();
        PagingBtn pagingBtn = new PagingBtn(totalRestaurants, pageNumber);
        int start = (pageNumber - 1) * size;
        int end = Math.min(start + size - 1, totalRestaurants);
        List<Restaurant> restaurants = allRestaurants.subList(start, end);

        return new ApiResponse<>(ResponseStatus.SUCCESS, restaurants);
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getNearbyRestaurants(
            @RequestParam(defaultValue = "rating") String sortBy) {

        System.out.println(sortBy + "-------------------------");

        // 음식점 목록을 가져온다.
        List<Restaurant> restaurants = mainPageService.getNearbyRestaurants();

        // 음식점 목록을 정렬 (기본값은 'rating'으로 설정됨)
        switch (sortBy) {
            case "rating":
                restaurants.sort(Comparator.comparing(Restaurant::getRating).reversed()); // 별점 기준 내림차순
                break;
            case "reviewCount":
                restaurants.sort(Comparator.comparingInt(Restaurant::getReviewCount).reversed()); // 리뷰 수 기준 내림차순
                break;
            case "distance":
                restaurants.sort(Comparator.comparingDouble(Restaurant::getDistance)); // 거리 기준 오름차순
                break;
            default:
                // 기본값은 rating 기준으로 내림차순 정렬
                restaurants.sort(Comparator.comparing(Restaurant::getRating).reversed());
                break;
        }

        return restaurants;  // 정렬된 리스트 반환
    }
}
