package com.example.demo.utils;

import com.example.demo.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainPageRestaurantCache {

    private List<Restaurant> cachedRestaurants = new ArrayList<>();

    // 캐시된 음식점 리스트 반환
    public List<Restaurant> getCachedRestaurants() {
        return cachedRestaurants;
    }

    // 음식점 리스트 캐싱
    public void cacheRestaurants(List<Restaurant> restaurants) {
        this.cachedRestaurants = restaurants;
    }
}
