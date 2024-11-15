package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
//11.15 작성 - 경 
public class MainPageService {

    @Value("${google.api.key}")
    private String apiKey;

    private static final String PLACES_API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // RestTemplate과 ObjectMapper를 주입받는 생성자
    public MainPageService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Restaurant> getNearbyRestaurants(double latitude, double longitude) {
        List<Restaurant> restaurants = new ArrayList<>();
        String url = String.format("%s?location=%f,%f&radius=1000&type=restaurant&key=%s",
                PLACES_API_URL, latitude, longitude, apiKey);

        try {
            // Google Places API로부터 JSON 응답 가져오기
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            // "results" 배열에서 각 음식점 정보 추출
            JsonNode results = root.path("results");
            for (JsonNode node : results) {
                Restaurant restaurant = parseRestaurantFromJson(node);
                
                // 음식점의 위치 정보 가져오기
                double restaurantLat = node.path("geometry").path("location").path("lat").asDouble();
                double restaurantLng = node.path("geometry").path("location").path("lng").asDouble();

                // 현재 위치와 음식점 위치 사이의 거리 계산
                double distance = calculateDistance(latitude, longitude, restaurantLat, restaurantLng);
                restaurant.setDistance(distance);

                restaurants.add(restaurant);
            }

            // 거리 기준으로 오름차순 정렬
           restaurants.sort(Comparator.comparingDouble(Restaurant::getDistance));
            // 별점 기준으로 내림차순 정렬 (높은 순으로 정렬)
           //restaurants.sort(Comparator.comparingDouble(Restaurant::getRating).reversed());
           

        } catch (IOException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    // JSON 데이터를 Restaurant 객체로 변환하는 헬퍼 메서드
    private Restaurant parseRestaurantFromJson(JsonNode node) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(node.path("name").asText());
        restaurant.setRating(node.path("rating").asDouble());
        restaurant.setAddress(node.path("vicinity").asText());

        if (node.has("photos")) {
            String photoReference = node.path("photos").get(0).path("photo_reference").asText();
            String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",
                    photoReference, apiKey);
            restaurant.setPhotoUrl(photoUrl);
        }

        restaurant.setDistance(1.0); // 예시로 거리 설정, 필요시 추가 구현
        return restaurant;
    }
    
 // 두 지점 간의 거리 계산 (Haversine Formula)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // 지구의 반지름 (단위: km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // 거리 반환 (단위: km)
    }
    
}

// RestTemplate을 빈으로 등록하는 구성 클래스
@Configuration
class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
