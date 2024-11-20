package com.example.demo.utils;
import com.example.demo.model.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class GoogleNearByPlaceApi {

    @Value("${google.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Executor asyncExecutor = Executors.newFixedThreadPool(5); // 비동기 처리용 스레드풀
    private static final int DEFAULT_RADIUS = 1000; // 1km
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    private static final String FIELDS = "name,geometry,place_id,rating,user_ratings_total,photos,vicinity,opening_hours,price_level,formatted_phone_number";
    private static final String LANGUAGE = "ko"; // 한국어

    public GoogleNearByPlaceApi(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // 첫 번째 페이지 데이터 가져오기
    public List<Restaurant> fetchInitialNearbyRestaurants(String keyword) {
        PageData pageData = fetchRestaurantsByPage(keyword, null);
        return pageData.getRestaurants();
    }

    // 백그라운드에서 나머지 페이지 데이터 로드
    @Async
    public CompletableFuture<List<Restaurant>> fetchAllPagesAsync(String keyword) {
        List<Restaurant> allRestaurants = new ArrayList<>();
        String nextPageToken = null;

        try {
            while (true) {
                PageData pageData = fetchRestaurantsByPage(keyword, nextPageToken);
                allRestaurants.addAll(pageData.getRestaurants());
                nextPageToken = pageData.getNextPageToken();

                // 더 이상 페이지가 없으면 중단
                if (nextPageToken == null) {
                    break;
                }

                // Google Places API의 next_page_token 활성화를 기다림 (최대 2초)
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(allRestaurants);
    }

    // 페이지 데이터 가져오기
    private PageData fetchRestaurantsByPage(String keyword, String nextPageToken) {
        try {
            String url = buildUrl(keyword, nextPageToken);
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.path("results");

            List<Restaurant> restaurantList = new ArrayList<>();
            if (results.isArray()) {
                for (JsonNode node : results) {
                    CompletableFuture<Restaurant> future = fetchRestaurantDetails(node);
                    Restaurant restaurant = future.join();
                    if (restaurant != null) {
                        restaurantList.add(restaurant);
                    }
                }
            }

            String token = root.path("next_page_token").asText(null);
            return new PageData(restaurantList, token);

        } catch (Exception e) {
            e.printStackTrace();
            return new PageData(new ArrayList<>(), null);
        }
    }

    // 병렬로 상세 정보 가져오기
    private CompletableFuture<Restaurant> fetchRestaurantDetails(JsonNode node) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Restaurant restaurant = new Restaurant();
                restaurant.setName(node.path("name").asText());
                restaurant.setPlaceId(node.path("place_id").asText());
                restaurant.setRating(node.path("rating").asDouble(0.0));
                restaurant.setReviewCount(node.path("user_ratings_total").asInt(0));
                restaurant.setAddress(node.path("vicinity").asText());
                restaurant.setPhoneNumber(node.path("formatted_phone_number").asText(null)); // 전화번호
                restaurant.setPriceLevel(node.path("price_level").asInt(-1)); // 가격 정보
                restaurant.setOpenNow(node.path("opening_hours").path("open_now").asBoolean(false)); // 운영 상태

                // 사진 URL 설정
                if (node.has("photos")) {
                    String photoReference = node.path("photos").get(0).path("photo_reference").asText();
                    restaurant.setPhotoUrl(buildPhotoUrl(photoReference));
                }

                // 거리 계산 (geometry.location 사용)
                double restaurantLat = node.path("geometry").path("location").path("lat").asDouble();
                double restaurantLng = node.path("geometry").path("location").path("lng").asDouble();
                restaurant.setDistance(calculateDistance(35.159707, 129.060186, restaurantLat, restaurantLng));

                return restaurant;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }, asyncExecutor);
    }

    
    private String buildPhotoUrl(String photoReference) {
        return String.format(
            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&photoreference=%s&key=%s",
            photoReference, API_KEY
        );
    }


    // URL 생성 메서드
    private String buildUrl(String keyword, String nextPageToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("location", "35.159707,129.060186")
                .queryParam("radius", DEFAULT_RADIUS)
                .queryParam("type", "restaurant")
                .queryParam("language", LANGUAGE)
                .queryParam("fields", FIELDS)
                .queryParam("key", API_KEY);

        if (keyword != null) {
            builder.queryParam("keyword", keyword);
        }
        if (nextPageToken != null) {
            builder.queryParam("pagetoken", nextPageToken);
        }

        return builder.toUriString();
    }
    
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371000; // 지구 반지름 (단위: m)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c; // 거리 반환 (미터 단위)
    }


    // 내부 클래스: 페이지 데이터
    private static class PageData {
        private final List<Restaurant> restaurants;
        private final String nextPageToken;

        public PageData(List<Restaurant> restaurants, String nextPageToken) {
            this.restaurants = restaurants;
            this.nextPageToken = nextPageToken;
        }

        public List<Restaurant> getRestaurants() {
            return restaurants;
        }

        public String getNextPageToken() {
            return nextPageToken;
        }
    }
}
