package com.example.demo.service;

import com.example.demo.model.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MainPageService {

	@Value("${google.api.key}")
	private String API_KEY;

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	private static final String PLACES_API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	private static final int DEFAULT_RADIUS = 1000; // 1km
	private static final String DEFAULT_TYPE = "restaurant";
	private static final double FIXED_LATITUDE = 35.159707;
	private static final double FIXED_LONGITUDE = 129.060186;
	private static final int PHOTO_MAX_WIDTH = 300;
	private String nextPageToken;

	private List<Restaurant> cachedRestaurants = new ArrayList<>(); // 전체 데이터를 캐싱
	private boolean isDataLoaded = false; // 데이터가 이미 로드되었는지 확인
	private int apiRequestCount = 0; // API 요청 횟수

	public MainPageService(RestTemplate restTemplate, ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}

	// 모든 데이터를 반환 (필요 시 캐싱)
	public List<Restaurant> getNearbyRestaurants() {
		if (!isDataLoaded) {
			loadAllNearbyRestaurants();
		}
		return cachedRestaurants;
	}

	// 전체 데이터를 API 호출을 통해 가져와 캐시에 저장
	private void loadAllNearbyRestaurants() {
		List<JsonNode> placeData;
		String pageToken = null;

		do {
			placeData = getLocationBasedData(FIXED_LATITUDE, FIXED_LONGITUDE, DEFAULT_RADIUS, DEFAULT_TYPE, pageToken);

			if (placeData != null) {
				for (JsonNode node : placeData) {
					Restaurant restaurant = parseRestaurantFromJson(node);

					// 거리 계산 및 추가 데이터 처리
					double restaurantLat = node.path("geometry").path("location").path("lat").asDouble();
					double restaurantLng = node.path("geometry").path("location").path("lng").asDouble();
					double distance = calculateDistance(FIXED_LATITUDE, FIXED_LONGITUDE, restaurantLat, restaurantLng);
					restaurant.setDistance(distance);

					getPlaceDetails(restaurant);
					cachedRestaurants.add(restaurant);
				}
			}

			// 다음 페이지 토큰 업데이트
			pageToken = (placeData == null || placeData.isEmpty()) ? null : nextPageToken;

		} while (pageToken != null && !pageToken.isEmpty()); // 다음 페이지가 없을 때까지 반복

		// 거리순으로 정렬
		cachedRestaurants.sort(Comparator.comparingDouble(Restaurant::getDistance));
		isDataLoaded = true; // 데이터 로드 완료
	}

	// Google Places API 호출 메서드
	private List<JsonNode> getLocationBasedData(double latitude, double longitude, int radius, String type,
			String pageToken) {
		List<JsonNode> placeData = new ArrayList<>();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PLACES_API_URL)
				.queryParam("location", latitude + "," + longitude).queryParam("radius", radius)
				.queryParam("type", type).queryParam("language", "ko").queryParam("key", API_KEY);

		if (pageToken != null) {
			builder.queryParam("pagetoken", pageToken);
		}

		String url = builder.toUriString();
		apiRequestCount++;

		try {
			String response = restTemplate.getForObject(url, String.class);
			JsonNode root = objectMapper.readTree(response);
			JsonNode results = root.path("results");

			for (JsonNode node : results) {
				placeData.add(node);
			}

			// 다음 페이지 토큰 가져오기
			nextPageToken = root.path("next_page_token").asText();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return placeData;
	}

	// JSON 데이터를 Restaurant 객체로 변환
	private Restaurant parseRestaurantFromJson(JsonNode node) {
		Restaurant restaurant = new Restaurant();
		restaurant.setName(node.path("name").asText());
		restaurant.setRating(node.path("rating").asDouble(0.0));
		restaurant.setAddress(node.path("vicinity").asText());
		restaurant.setPhotoUrl(getPhotoUrl(node));
		return restaurant;
	}

	// Google Places API에서 사진 URL 생성
	private String getPhotoUrl(JsonNode node) {
		if (node.has("photos")) {
			String photoReference = node.path("photos").get(0).path("photo_reference").asText();
			return String.format(
					"https://maps.googleapis.com/maps/api/place/photo?maxwidth=%d&photoreference=%s&key=%s",
					PHOTO_MAX_WIDTH, photoReference, API_KEY);
		}
		return null;
	}

	// Google Places Detail API를 통해 추가 세부정보 가져오기
	private void getPlaceDetails(Restaurant restaurant) {
		String placeDetailsUrl = "https://maps.googleapis.com/maps/api/place/details/json";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(placeDetailsUrl)
				.queryParam("place_id", restaurant.getPlaceId())
				.queryParam("fields", "formatted_phone_number,opening_hours/open_now,opening_hours/weekday_text")
				.queryParam("key", API_KEY);

		try {
			String response = restTemplate.getForObject(builder.toUriString(), String.class);
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

				if (openingHoursNode.has("weekday_text")) {
					List<String> hours = new ArrayList<>();
					openingHoursNode.path("weekday_text").forEach(day -> hours.add(day.asText()));
					restaurant.setOpeningHours(hours);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 좌표 간 거리 계산
	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		final int EARTH_RADIUS = 6371000; // 지구 반지름 (단위: m)
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS * c;
	}

	// API 요청 횟수 반환
	public int getApiRequestCount() {
		return apiRequestCount;
	}
}
