package com.example.demo.service;

import com.example.demo.model.Restaurant;
import com.example.demo.utils.GoogleNearByPlaceApi;
import com.example.demo.utils.MainPageRestaurantCache;
import com.example.demo.utils.MainPageRestaurantMapper;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainPageService {

	private final GoogleNearByPlaceApi googleNearByPlaceApi;
	private final MainPageRestaurantCache restaurantCache;
	private boolean isDataLoaded = false;

	public MainPageService(GoogleNearByPlaceApi googleNearByPlaceApi, MainPageRestaurantCache restaurantCache) {
		this.googleNearByPlaceApi = googleNearByPlaceApi;
		this.restaurantCache = restaurantCache;
	}

	public List<Restaurant> getNearbyRestaurants() {
		if (!isDataLoaded) {
			loadAllNearbyRestaurants();
		}
		return restaurantCache.getCachedRestaurants();
	}

	public List<Restaurant> getNearbyRestaurants(String sortBy) {
		if (!isDataLoaded) {
			loadAllNearbyRestaurants();
		}
		return googleNearByPlaceApi.sortRestaurants(restaurantCache.getCachedRestaurants(), sortBy);
	}

	// 모든 음식점 데이터 로드 및 캐시
	private void loadAllNearbyRestaurants() {
		List<Restaurant> restaurants = googleNearByPlaceApi.fetchAllNearbyRestaurants("", "rating");
		restaurantCache.cacheRestaurants(restaurants);
		isDataLoaded = true;
	}
}
