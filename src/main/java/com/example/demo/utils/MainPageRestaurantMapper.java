package com.example.demo.utils;

import com.example.demo.model.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MainPageRestaurantMapper {

	@Value("${google.api.key}")
	private String API_KEY;

	private static final int PHOTO_MAX_WIDTH = 300;

	// JSON 데이터를 Restaurant 객체로 변환
	public Restaurant mapJsonToRestaurant(JsonNode node) {
		Restaurant restaurant = new Restaurant();
		restaurant.setName(node.path("name").asText());
		restaurant.setRating(node.path("rating").asDouble(0.0));
		restaurant.setAddress(node.path("vicinity").asText());
		restaurant.setPlaceId(node.path("place_id").asText());

		// 사진 URL 설정
		String photoUrl = getPhotoUrl(node);
		restaurant.setPhotoUrl(photoUrl);

		// 세부 정보 추가
		restaurant.setPhoneNumber(getPhoneNumber(node));
		restaurant.setPriceLevel(getPriceLevel(node));
		restaurant.setWebsite(getWebsite(node));
		restaurant.setReviewCount(getReviewCount(node)); // 리뷰 수 추가

		// 거리 계산 및 설정
		double restaurantLat = node.path("geometry").path("location").path("lat").asDouble();
		double restaurantLng = node.path("geometry").path("location").path("lng").asDouble();
		double distance = calculateDistance(35.159707, 129.060186, restaurantLat, restaurantLng);
		restaurant.setDistance(distance);

		return restaurant;
	}

	// 사진 URL 생성
	private String getPhotoUrl(JsonNode node) {
		if (node.has("photos")) {
			String photoReference = node.path("photos").get(0).path("photo_reference").asText();
			return String.format(
					"https://maps.googleapis.com/maps/api/place/photo?maxwidth=%d&photoreference=%s&key=%s",
					PHOTO_MAX_WIDTH, photoReference, API_KEY);
		}
		return null;
	}

	// 전화번호 추출
	private String getPhoneNumber(JsonNode node) {
		if (node.has("formatted_phone_number")) {
			return node.path("formatted_phone_number").asText();
		}
		return null;
	}

	// 운영 시간 추출
	private String getOpeningHours(JsonNode node) {
		if (node.has("opening_hours")) {
			JsonNode hoursNode = node.path("opening_hours").path("weekday_text");
			if (hoursNode.isArray() && hoursNode.size() > 0) {
				StringBuilder hours = new StringBuilder();
				hoursNode.forEach(hour -> hours.append(hour.asText()).append("\n"));
				return hours.toString();
			}
		}
		return null;
	}

	// 가격 수준 추출
	private int getPriceLevel(JsonNode node) {
		if (node.has("price_level")) {
			return node.path("price_level").asInt();
		}
		return -1; // 가격 수준이 없는 경우
	}

	// 웹사이트 URL 추출
	private String getWebsite(JsonNode node) {
		if (node.has("website")) {
			return node.path("website").asText();
		}
		return null;
	}

	// 리뷰 수 합계 추출
	private int getReviewCount(JsonNode node) {
		if (node.has("user_ratings_total")) {
			return node.path("user_ratings_total").asInt();
		}
		return 0; // 리뷰 수가 없는 경우 0 반환
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
}
