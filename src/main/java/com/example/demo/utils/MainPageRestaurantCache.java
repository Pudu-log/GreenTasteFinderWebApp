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
*/

@Component
public class MainPageRestaurantCache {

    private final Map<String, List<Restaurant>> cache = new ConcurrentHashMap<>();

    public Optional<List<Restaurant>> getCachedRestaurants(String keyword) {
        return Optional.ofNullable(cache.get(keyword));
    }

    public void cacheRestaurants(String keyword, List<Restaurant> restaurants) {
        cache.put(keyword, restaurants);
    }

    public void clearCache() {
        cache.clear();
    }
}

