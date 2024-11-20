package com.example.demo.service;

import com.example.demo.model.Restaurant;
import com.example.demo.utils.GoogleNearByPlaceApi;
import com.example.demo.utils.MainPageRestaurantCache;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class MainPageService {

    private final GoogleNearByPlaceApi googleNearByPlaceApi;
    private final MainPageRestaurantCache restaurantCache;

    public MainPageService(GoogleNearByPlaceApi googleNearByPlaceApi,
                           MainPageRestaurantCache restaurantCache) {
        this.googleNearByPlaceApi = googleNearByPlaceApi;
        this.restaurantCache = restaurantCache;
    }

    /**
     * 초기 데이터를 가져옵니다. 캐시에 데이터가 있으면 반환, 없으면 Google API에서 로드 후 캐시에 저장.
     *
     * @param keyword 검색 키워드
     * @return 초기 레스토랑 데이터 리스트
     */
    public List<Restaurant> fetchInitialRestaurants(String keyword) {
        return restaurantCache.getCachedRestaurants(keyword).orElseGet(() -> {
            List<Restaurant> restaurants = googleNearByPlaceApi.fetchInitialNearbyRestaurants(keyword);
            restaurantCache.cacheRestaurants(keyword, restaurants);
            return restaurants;
        });
    }

    /**
     * 모든 레스토랑 데이터를 비동기로 로드합니다.
     *
     * @param keyword 검색 키워드
     * @return CompletableFuture로 모든 레스토랑 데이터 반환
     */
    @Async
    public CompletableFuture<List<Restaurant>> fetchAllRestaurants(String keyword) {
        return googleNearByPlaceApi.fetchAllPagesAsync(keyword)
                .thenApply(restaurants -> {
                    if (restaurants.isEmpty()) {
                        System.out.println("음식점 데이터가 비어 있습니다.");
                    }
                    restaurantCache.cacheRestaurants(keyword, restaurants);
                    return restaurants;
                })
                .exceptionally(ex -> {
                    ex.printStackTrace(); // 오류 로그 출력
                    return List.of(); // 빈 리스트 반환
                });
    }
}
