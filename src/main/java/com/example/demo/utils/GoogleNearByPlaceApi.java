package com.example.demo.utils;

import com.example.demo.model.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GoogleNearByPlaceApi {

    @Value("${google.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final MainPageRestaurantMapper mainPageRestaurantMapper;

    private static final String PLACES_API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    private static final String PLACES_DETAIL_API_URL = "https://maps.googleapis.com/maps/api/place/details/json";
    private static final int PHOTO_MAX_WIDTH = 300;
    private static final int DEFAULT_RADIUS = 3000; // 3km
    private static final String DEFAULT_TYPE = "restaurant";
    private static final double FIXED_LATITUDE = 35.159707;
    private static final double FIXED_LONGITUDE = 129.060186;

    private String nextPageToken;

    public GoogleNearByPlaceApi(RestTemplate restTemplate, ObjectMapper objectMapper, MainPageRestaurantMapper mainPageRestaurantMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.mainPageRestaurantMapper = mainPageRestaurantMapper;
    }

    // 주어진 검색어와 정렬 기준을 포함하여 음식점 목록을 가져옵니다.
    public List<Restaurant> fetchAllNearbyRestaurants(String keyword, String sortBy) {
        List<Restaurant> restaurants = new ArrayList<>();
        String pageToken = null;

        do {
            // 데이터 받아오기 (검색어 필터링 추가)
            List<JsonNode> placeData = getLocationBasedData(FIXED_LATITUDE, FIXED_LONGITUDE, DEFAULT_RADIUS, DEFAULT_TYPE, pageToken, keyword);

            if (placeData != null && !placeData.isEmpty()) {
                placeData.forEach(node -> {
                    // JSON 데이터를 Restaurant 객체로 변환
                    Restaurant restaurant = mainPageRestaurantMapper.mapJsonToRestaurant(node);

                    // 상세 정보 가져오기 (전화번호, 영업시간 등)
                    getPlaceDetails(restaurant);

                    // 음식점 리스트에 추가
                    restaurants.add(restaurant);
                });
            }

            // 다음 페이지 토큰을 추출
            pageToken = nextPageToken;

        } while (pageToken != null && !pageToken.isEmpty());  // 페이지 토큰이 존재하면 계속해서 추가 데이터 요청

        // 정렬 기준에 맞게 음식점 리스트 정렬
        return sortRestaurants(restaurants, sortBy);
    }

    // 검색어를 포함한 장소 데이터를 받아오는 메서드
    public List<JsonNode> getLocationBasedData(double latitude, double longitude, int radius, String type, String pageToken, String keyword) {
        List<JsonNode> placeData = new ArrayList<>();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLACES_API_URL)
                .queryParam("location", latitude + "," + longitude)
                .queryParam("radius", radius)
                .queryParam("type", type)
                .queryParam("language", "ko")
                .queryParam("key", API_KEY);

        // 키워드를 검색 조건에 추가
        if (keyword != null && !keyword.isEmpty()) {
            builder.queryParam("keyword", keyword);
        }

        if (pageToken != null) {
            builder.queryParam("pagetoken", pageToken);
        }

        String url = builder.toUriString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.path("results");

            results.forEach(placeData::add);
            nextPageToken = root.path("next_page_token").asText();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return placeData;
    }

    // Google Place Details API를 호출하여 상세 정보를 가져오는 메서드
    public void getPlaceDetails(Restaurant restaurant) {
        String placeDetailsUrl = UriComponentsBuilder.fromHttpUrl(PLACES_DETAIL_API_URL)
                .queryParam("place_id", restaurant.getPlaceId())
                .queryParam("fields", "formatted_phone_number,opening_hours/weekday_text")
                .queryParam("key", API_KEY)
                .toUriString();

        try {
            String response = restTemplate.getForObject(placeDetailsUrl, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode result = root.path("result");

            if (result.has("formatted_phone_number")) {
                restaurant.setPhoneNumber(result.path("formatted_phone_number").asText());
            }

            if (result.has("opening_hours")) {
                JsonNode openingHoursNode = result.path("opening_hours");

                if (openingHoursNode.has("open_now")) {
                    restaurant.setOpenNow(openingHoursNode.path("open_now").asBoolean());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 음식점 리스트를 정렬하는 메서드
    public List<Restaurant> sortRestaurants(List<Restaurant> restaurants, String sortBy) {
        switch (sortBy) {
            case "rating":
                return restaurants.stream()
                        .sorted(Comparator.comparingDouble(Restaurant::getRating).reversed()) // 별점순 정렬
                        .collect(Collectors.toList());

            case "reviewCount":
                return restaurants.stream()
                        .sorted(Comparator.comparingInt(Restaurant::getReviewCount).reversed()) // 리뷰 수순 정렬
                        .collect(Collectors.toList());

            case "distance":
                return restaurants.stream()
                        .sorted(Comparator.comparingDouble(Restaurant::getDistance)) // 거리순 정렬
                        .collect(Collectors.toList());

            default:
                return restaurants;
        }
    }
}
