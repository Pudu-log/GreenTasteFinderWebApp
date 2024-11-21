package com.example.demo.utils;

import com.example.demo.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
/*
작성자: 구경림  
작성일: 2024.11.20  
작성이유:  
1. 캐싱을 통해 서버 부담을 줄이고 데이터 접근 속도를 향상.  
2. 자주 사용되는 음식점 데이터를 메모리에서 관리.  

*/

@Component
public class MainPageRestaurantCache {

    private final Map<String, List<Restaurant>> cache = new ConcurrentHashMap<>();

    // 캐시에서 레스토랑 데이터를 가져옴
    public Optional<List<Restaurant>> getCachedRestaurants(String keyword) {
        return Optional.ofNullable(cache.get(keyword));
    }

    // 캐시에 레스토랑 데이터를 저장
    public void cacheRestaurants(String keyword, List<Restaurant> restaurants) {
        cache.put(keyword, restaurants);
    }

    // 캐시 초기화
    public void clearCache() {
        cache.clear();
    }
}

