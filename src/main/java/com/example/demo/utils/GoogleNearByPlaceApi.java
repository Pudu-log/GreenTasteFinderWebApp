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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 구 경림
 *
 * 이 클래스는 Google Places API를 사용하여 주변 음식점 데이터를 가져오는 유틸리티 컴포넌트입니다.
 * 
 * 
 * 1. Google Places API를 호출하여 주변 음식점을 검색 2. 상세 정보(전화번호, 영업시간 등)를 추가로 가져오기 위해
 * Place Details API 호출 3. 검색 결과를 정렬(평점, 리뷰 수, 거리 등 기준)하여 반환 4. API 호출 결과를 캐시에
 * 저장하여 반복 호출 최소화
 * 
 */
@Component
public class GoogleNearByPlaceApi {
	
	@Value("${google.api.key}")
	private String API_KEY;

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	private final MainPageRestaurantMapper mainPageRestaurantMapper;

	private final Map<String, List<Restaurant>> cachedResults = new ConcurrentHashMap<>();

	private static final int PHOTO_MAX_WIDTH = 300;
	private static final int DEFAULT_RADIUS = 1000; //1km
	private static final double FIXED_LATITUDE = 35.159707;
	private static final double FIXED_LONGITUDE = 129.060186;
	private static final String PLACES_API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	private static final String PLACES_DETAIL_API_URL = "https://maps.googleapis.com/maps/api/place/details/json";
	private static final String DEFAULT_TYPE = "restaurant";

	private String nextPageToken;
	
	public GoogleNearByPlaceApi(RestTemplate restTemplate, ObjectMapper objectMapper,
			MainPageRestaurantMapper mainPageRestaurantMapper) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
		this.mainPageRestaurantMapper = mainPageRestaurantMapper;
	}
	
	//주변 음식점을 검색어와 정렬 기준에 따라 조회하고 캐시에 저장.
	public List<Restaurant> fetchAllNearbyRestaurants(String keyword, String sortBy) {
		String cacheKey = (keyword == null ? "all" : keyword) + "_" + sortBy;

		// 캐시에 데이터가 있는지 확인
		if (cachedResults.containsKey(cacheKey)) {
			return cachedResults.get(cacheKey);
		}

		List<Restaurant> restaurants = new ArrayList<>();
		String pageToken = null;

		do {
			// 데이터 받아오기 (검색어 필터링 추가)
			List<JsonNode> placeData = getLocationBasedData(DEFAULT_TYPE, pageToken, keyword);

			if (placeData != null && !placeData.isEmpty()) {
				placeData.forEach(node -> {
					Restaurant restaurant = mainPageRestaurantMapper.mapJsonToRestaurant(node);
					// 상세 정보 가져오기 (전화번호, 영업시간 등)
					getPlaceDetails(restaurant);
					restaurants.add(restaurant);
				});
			}

			// 다음 페이지 토큰을 추출
			pageToken = nextPageToken;

		} while (pageToken != null && !pageToken.isEmpty()); // 페이지 토큰이 존재하면 계속해서 추가 데이터 요청

		// 정렬 기준에 맞게 음식점 리스트 정렬
		List<Restaurant> sortedRestaurants = sortRestaurants(restaurants, sortBy);
		// 캐시에 저장
		cachedResults.put(cacheKey, sortedRestaurants);

		return sortedRestaurants;
	}

	//정렬 및 음식점 리스트 반환
	public List<Restaurant> sortRestaurants(List<Restaurant> restaurants, String sortBy) {
	    if (restaurants == null || restaurants.isEmpty()) {
	        return restaurants; // 음식점 리스트가 없으면 그대로 반환
	    }

	    switch (sortBy) {
	        case "rating":
	            // 평점 기준으로 내림차순 정렬
	            return restaurants.stream()
	                    .sorted(Comparator.comparingDouble(Restaurant::getRating).reversed())
	                    .collect(Collectors.toList());
	        case "reviewCount":
	            // 리뷰 수 기준으로 내림차순 정렬
	            return restaurants.stream()
	                    .sorted(Comparator.comparingInt(Restaurant::getReviewCount).reversed())
	                    .collect(Collectors.toList());
	        case "distance":
	            // 거리 기준으로 오름차순 정렬
	            return restaurants.stream()
	                    .sorted(Comparator.comparingDouble(Restaurant::getDistance))
	                    .collect(Collectors.toList());
	        default:
	            // 기본값은 평점 기준으로 내림차순 정렬
	            return restaurants.stream()
	                    .sorted(Comparator.comparingDouble(Restaurant::getRating).reversed())
	                    .collect(Collectors.toList());
	    }
	}

	// Google Place Details API를 호출하여 상세 정보를 가져오는 메서드
	public void getPlaceDetails(Restaurant restaurant) {
		String placeDetailsUrl = UriComponentsBuilder.fromHttpUrl(PLACES_DETAIL_API_URL)
				.queryParam("place_id", restaurant.getPlaceId())
				.queryParam("fields", "formatted_phone_number,opening_hours/weekday_text").queryParam("key", API_KEY)
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
	
	// 위치, 반경, 키워드, 페이지 토큰 등을 이용해 Google Places API 호출
	public List<JsonNode> getLocationBasedData(String type, String pageToken, String keyword) {
	    List<JsonNode> placeData = new ArrayList<>();
	    try {
	        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(PLACES_API_URL)
	                .queryParam("location", FIXED_LATITUDE + "," + FIXED_LONGITUDE)  // 고정된 위도와 경도
	                .queryParam("radius", DEFAULT_RADIUS)  // 기본 반경 설정 (1000m)
	                .queryParam("type", type)  // 장소 유형 (예: restaurant)
	                .queryParam("keyword", keyword)  // 키워드로 필터링 (선택사항)
	                .queryParam("key", API_KEY);  // Google API Key
	        
	        // 페이지 토큰이 있는 경우 추가
	        if (pageToken != null && !pageToken.isEmpty()) {
	            uriBuilder.queryParam("pagetoken", pageToken);
	        }

	        // API 호출
	        String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
	        JsonNode rootNode = objectMapper.readTree(response);

	        // 결과 리스트 추출
	        JsonNode results = rootNode.path("results");
	        if (results.isArray()) {
	            results.forEach(placeData::add);
	        }

	        // 페이지 토큰 처리
	        nextPageToken = rootNode.path("next_page_token").asText(null);  // 다음 페이지 토큰 저장

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return placeData;
	}

}
