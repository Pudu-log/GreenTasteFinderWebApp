package com.example.demo.utils;

import com.example.demo.model.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/*
작성자: 구경림  
작성일: 2024.11.20  
작성이유:  
1. Google Places API 응답 데이터를 `Restaurant` 객체로 변환하여 재사용성을 높임.  
2. 공통된 데이터 추출 로직을 모듈화하여 유지보수성을 강화.  
.  
*/

@Component
public class MainPageRestaurantMapper {

    @Value("${google.api.key}")
    private String API_KEY;

    private static final int PHOTO_MAX_WIDTH = 300;

    /**
     * JsonNode 데이터를 Restaurant 객체로 변환합니다.
     * 
     * @param node Google Places API에서 반환된 JsonNode
     * @return 변환된 Restaurant 객체
     */
    public Restaurant mapJsonToRestaurant(JsonNode node) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(getTextValue(node, "name"));
        restaurant.setRating(getDoubleValue(node, "rating", 0.0));
        restaurant.setAddress(getTextValue(node, "vicinity"));
        restaurant.setPlaceId(getTextValue(node, "place_id"));

        // 사진 URL 설정
        restaurant.setPhotoUrl(getPhotoUrl(node));

        // 세부 정보 추가
        restaurant.setPhoneNumber(getPhoneNumber(node));
        restaurant.setPriceLevel(getIntValue(node, "price_level", -1));
        restaurant.setWebsite(getTextValue(node, "website"));
        restaurant.setReviewCount(getIntValue(node, "user_ratings_total", 0));
        restaurant.setOpeningHours(getOpeningHoursList(node)); // 추가

        // 거리 계산 및 설정
        double restaurantLat = node.path("geometry").path("location").path("lat").asDouble();
        double restaurantLng = node.path("geometry").path("location").path("lng").asDouble();
        restaurant.setDistance(calculateDistance(35.159707, 129.060186, restaurantLat, restaurantLng));

        return restaurant;
    }
    // 운영 시간 추출 (List<String> 반환)
    private List<String> getOpeningHoursList(JsonNode node) {
        if (node.has("opening_hours") && node.path("opening_hours").has("weekday_text")) {
            JsonNode hoursNode = node.path("opening_hours").path("weekday_text");
            List<String> openingHours = new ArrayList<>();
            hoursNode.forEach(hour -> openingHours.add(hour.asText()));
            return openingHours;
        }
        return List.of(); // 데이터가 없으면 빈 리스트 반환
    }
    /**
     * 사진 URL 생성
     */
    private String getPhotoUrl(JsonNode node) {
        if (node.has("photos")) {
            String photoReference = node.path("photos").get(0).path("photo_reference").asText();
            return String.format(
                    "https://maps.googleapis.com/maps/api/place/photo?maxwidth=%d&photoreference=%s&key=%s",
                    PHOTO_MAX_WIDTH, photoReference, API_KEY);
        }
        return null;
    }

    /**
     * 전화번호 추출
     */
    private String getPhoneNumber(JsonNode node) {
        return getTextValue(node, "formatted_phone_number");
    }

    /**
     * 운영 시간 추출 (사용하지 않는 경우 제거 가능)
     */
    private String getOpeningHours(JsonNode node) {
        if (node.has("opening_hours")) {
            JsonNode hoursNode = node.path("opening_hours").path("weekday_text");
            if (hoursNode.isArray()) {
                StringBuilder hours = new StringBuilder();
                hoursNode.forEach(hour -> hours.append(hour.asText()).append("\n"));
                return hours.toString().trim();
            }
        }
        return null;
    }

    /**
     * 좌표 간 거리 계산
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371000; // 지구 반지름 (단위: m)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    /**
     * 공통 JsonNode 텍스트 값 추출 메서드
     */
    private String getTextValue(JsonNode node, String fieldName) {
        return node.has(fieldName) ? node.path(fieldName).asText() : "정보 없음";
    }
    /**
     * 공통 JsonNode 정수 값 추출 메서드
     */
    private int getIntValue(JsonNode node, String fieldName, int defaultValue) {
        if (node.has(fieldName)) {
            return node.path(fieldName).asInt(defaultValue);
        }
        return defaultValue;
    }

    /**
     * 공통 JsonNode 실수 값 추출 메서드
     */
    private double getDoubleValue(JsonNode node, String fieldName, double defaultValue) {
        if (node.has(fieldName)) {
            return node.path(fieldName).asDouble(defaultValue);
        }
        return defaultValue;
    }
}
