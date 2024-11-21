package com.example.demo.service;

import com.example.demo.dao.mainpage.IMainPageDao;
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
    private final IMainPageDao mainPageDao;

    public MainPageService(GoogleNearByPlaceApi googleNearByPlaceApi,
                           MainPageRestaurantCache restaurantCache,
                           IMainPageDao mainPageDao) {
        this.googleNearByPlaceApi = googleNearByPlaceApi;
        this.restaurantCache = restaurantCache;
        this.mainPageDao = mainPageDao;
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
//    public CompletableFuture<List<Restaurant>> fetchAllRestaurants(String keyword) {
//        return googleNearByPlaceApi.fetchAllPagesAsync(keyword)
//                .thenApply(restaurants -> {
//                    if (restaurants.isEmpty()) {
//                        System.out.println("음식점 데이터가 비어 있습니다.");
//                    }
//                    restaurantCache.cacheRestaurants(keyword, restaurants);
//                    return restaurants;
//                })
//                .exceptionally(ex -> {
//                    ex.printStackTrace(); // 오류 로그 출력
//                    return List.of(); // 빈 리스트 반환
//                });
//    }
    public CompletableFuture<List<Restaurant>> fetchAllRestaurants(String keyword) {
        return googleNearByPlaceApi.fetchAllPagesAsync(keyword)
                .thenApply(restaurants -> {
                    if (restaurants.isEmpty()) {
                        System.out.println("음식점 데이터가 비어 있습니다.");
                    }
                    restaurantCache.cacheRestaurants("restaurant", restaurants);
                    return restaurants;
                })
                .thenApply(restaurants -> {
                    // 사용자의 좋아요/즐겨찾기 상태 업데이트 (서비스 레이어에서 수행)
                    // 필요한 경우 memberId는 서비스 레이어에 추가적으로 전달받아야 함
                    return restaurants;
                });
    }

    
    // 사용자의 좋아요/즐겨찾기한 가게 목록 가져오기
    public List<String> getUserActions(String memberId, String gubn) {
        return mainPageDao.findUserActions(memberId, gubn);
    }

    // 좋아요/즐겨찾기 추가
    public void addUserAction(String memberId, String storeId, String gubn) {
        mainPageDao.insertAct(memberId, storeId, gubn);
    }

    // 좋아요/즐겨찾기 삭제
    public void removeUserAction(String memberId, String storeId, String gubn) {
        mainPageDao.deleteAct(memberId, storeId, gubn);
    }
}
