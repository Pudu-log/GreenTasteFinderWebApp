package com.example.demo.utils;

import com.example.demo.model.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* 
작성자: 구경림
작성일: 2024.11.20
Google Places API를 호출하여 음식점 데이터를 수집하고 가공하기 위한 유틸리티 클래스.
비동기 데이터 처리를 통해 초기 로딩 시간을 줄이고 전체 데이터를 병렬로 가져오기 위함.
위치 기반 거리 계산 및 Google API 응답 데이터를 모델 객체로 변환하여 데이터 가공.
*/
@Component
public class GoogleNearByPlaceApi {

	@Value("${google.api.key}")
	private String API_KEY;

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	private static final int DEFAULT_RADIUS = 1000; // 1km
	private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	private static final String FIELDS = "name,geometry,place_id,rating,user_ratings_total,photos,vicinity,opening_hours,price_level,business_status";
	private static final String LANGUAGE = "ko"; // 한국어
	private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/restaurants/";
	private static final String IMAGE_DIRECTORY_BIN = "bin/main/static/images/restaurants/";
	private static final AtomicInteger TOTAL_REQUEST_COUNT = new AtomicInteger(0); // 전체 요청 횟수 카운터
	private static final AtomicInteger REQUEST_COUNT = new AtomicInteger(0); // 페이지 요청 횟수 카운터

	public GoogleNearByPlaceApi(RestTemplate restTemplate, ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}

	/**
	 * 특정 키워드로 초기 음식점 데이터를 로드.
	 * 
	 * @param keyword 검색 키워드
	 * @return 음식점 리스트
	 */
	public List<Restaurant> fetchInitialNearbyRestaurants(String keyword) {
		PageData pageData = fetchRestaurantsByPage(keyword, null);
		return pageData.getRestaurants();
	}

	/**
	 * Google Places API를 사용해 여러 페이지의 음식점 데이터를 비동기로 가져옴.
	 * 
	 * @param keyword 검색 키워드
	 * @return CompletableFuture 형태의 전체 음식점 리스트
	 */
	@Async
	public CompletableFuture<List<Restaurant>> fetchAllPagesAsync(String keyword) {
		List<Restaurant> allRestaurants = new ArrayList<>();
		String nextPageToken = null;

		try {
			while (true) {
				PageData pageData = fetchRestaurantsByPage(keyword, nextPageToken);
				allRestaurants.addAll(pageData.getRestaurants());
				
				// Google Places API의 next_page_token 활성화를 기다림 (최대 2초)
				Thread.sleep(2000);

				nextPageToken = pageData.getNextPageToken();
				System.out.println("검색된 음식점 수" + allRestaurants.size());
                System.out.println("다음 페이지 토큰: " + nextPageToken);
                
				// 다음 페이지 토큰이 없으면 중단
				if (nextPageToken == null || nextPageToken.isBlank()) {
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return CompletableFuture.completedFuture(allRestaurants);
	}

	/**
	 * Google Places API를 호출하여 특정 페이지의 음식점 데이터를 가져옴.
	 * 
	 * @param keyword       검색 키워드
	 * @param nextPageToken 다음 페이지 토큰 (없으면 null)
	 * @return 현재 페이지의 음식점 데이터와 다음 페이지 토큰
	 */
	private PageData fetchRestaurantsByPage(String keyword, String nextPageToken) {
		try {
			int currentCount = REQUEST_COUNT.incrementAndGet();
			TOTAL_REQUEST_COUNT.incrementAndGet(); // 총 요청 횟수 증가
			System.out.println("페이지 API 요청 횟수: " + currentCount);
			System.out.println("총 API 요청 횟수: " + TOTAL_REQUEST_COUNT.get());

			String url = buildUrl(keyword, nextPageToken);
			String response = restTemplate.getForObject(url, String.class);

			JsonNode root = objectMapper.readTree(response);
			JsonNode results = root.path("results");

			List<Restaurant> restaurantList = new ArrayList<>();
			if (results.isArray()) {
				for (JsonNode node : results) {
					// Nearby Search 응답으로 레스토랑 정보 생성
					Restaurant restaurant = mapBasicDetails(node);
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

	/**
	 * Google Places API의 JSON 응답을 Restaurant 객체로 변환. - 음식점 기본 정보 매핑 - 사진 로컬 저장 및 경로
	 * 설정 - 위치 정보 매핑
	 * 
	 * @param node JSON 응답 노드
	 * @return 변환된 Restaurant 객체
	 */
	private Restaurant mapBasicDetails(JsonNode node) {
		try {
			Restaurant restaurant = new Restaurant();
			restaurant.setName(node.path("name").asText("정보 없음"));
			restaurant.setPlaceId(node.path("place_id").asText("정보 없음"));
			restaurant.setRating(node.path("rating").asDouble(0.0));
			restaurant.setReviewCount(node.path("user_ratings_total").asInt(0));
			restaurant.setAddress(node.path("vicinity").asText("정보 없음"));
			restaurant.setPriceLevel(node.path("price_level").asInt(-1));
			restaurant.setBusinessStatus(node.path("business_status").asText("정보 없음"));

			// 운영 시간 정보
			boolean openNow = node.path("opening_hours").path("open_now").asBoolean(false);
			restaurant.setOpenNow(openNow);

			// 사진 URL
			if (node.has("photos")) {
				String photoReference = node.path("photos").get(0).path("photo_reference").asText();
				String placeId = node.path("place_id").asText();

				// 로컬에 사진 저장 및 업데이트 확인
				updatePhotoIfChanged(photoReference, placeId);

				// 로컬 사진 경로를 Restaurant 객체에 설정
				restaurant.setPhotoUrl("/static/images/restaurants/" + placeId + ".jpg"); // 로컬 URL 설정
			}

			// 위치 정보 (거리 계산 포함)
			double restaurantLat = node.path("geometry").path("location").path("lat").asDouble();
			double restaurantLng = node.path("geometry").path("location").path("lng").asDouble();
			restaurant.setDistance(calculateDistance(35.159707, 129.060186, restaurantLat, restaurantLng));

			return restaurant;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 서버 실행 시 동기화 진행 메서드. (빌드 후 bin에 이미지들.. 자동 복사가 제대로 이루어지지 않아서 직접 작성함..)
	 * 
	 * @PostConstruct 어노테이션을 사용하여 서버가 시작될 때 자동으로 호출됩니다. 이미지 디렉토리 내의 모든 파일을
	 *                IMAGE_DIRECTORY_BIN으로 동기화합니다.
	 */
	@PostConstruct
	public void init() {
		System.out.println("서버 시작: 이미지 디렉토리 동기화 시작");
		syncAllFilesToBin();
	}

	/**
	 * 사진을 로컬에 저장.
	 * 
	 * @param photoReference Google Places API 사진 참조 키
	 * @param placeId        음식점 ID
	 */
	private void savePhoto(String photoReference, String placeId) {
		try {
			// 사진 URL 생성
			String photoUrl = buildPhotoUrl(photoReference);

			// 카운터 증가: 실제로 사진 요청이 이루어질 때만
			TOTAL_REQUEST_COUNT.incrementAndGet();

			System.out.println("사진 요청 URL: " + photoUrl);
			System.out.println("총 API 요청 횟수: " + TOTAL_REQUEST_COUNT.get());

			// 로컬 파일 경로
			String filePath = IMAGE_DIRECTORY + placeId + ".jpg";
			String binFilePath = IMAGE_DIRECTORY_BIN + placeId + ".jpg";

			File file = new File(filePath);

			// 파일이 이미 존재하면 저장하지 않음
			if (file.exists()) {
				System.out.println("이미 존재하는 이미지: " + filePath);
				// 동기화 체크
				syncFileToBin(filePath, binFilePath);
				return;
			}

			// Google Places API로부터 이미지 다운로드
			System.out.println("사진 다운로드 시작: " + photoUrl);
			URL url = new URL(photoUrl);
			try (FileOutputStream fos = new FileOutputStream(file)) {
				byte[] buffer = url.openStream().readAllBytes();
				fos.write(buffer);
				System.out.println("사진 저장 완료: " + filePath);
			}

			// 새로 저장된 파일을 bin 디렉토리로 복사
			syncFileToBin(filePath, binFilePath);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("사진 저장 실패: " + photoReference);
		}
	}

	/**
	 * 특정 파일을 IMAGE_DIRECTORY_BIN으로 복사합니다.
	 *
	 * @param sourcePath 원본 파일 경로
	 * @param targetPath 대상 파일 경로
	 */
	private void syncFileToBin(String sourcePath, String targetPath) {
		try {
			Path source = Path.of(sourcePath);
			Path target = Path.of(targetPath);

			// 부모 디렉토리가 없으면 생성
			File targetDir = new File(IMAGE_DIRECTORY_BIN);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}

			// 파일 복사
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("이미지 복사 완료: " + targetPath);
		} catch (IOException e) {
			System.err.println("이미지 복사 중 오류 발생: " + e.getMessage());
		}
	}

	/**
	 * IMAGE_DIRECTORY의 모든 파일을 IMAGE_DIRECTORY_BIN으로 동기화합니다.
	 */
	public void syncAllFilesToBin() {
		try (Stream<Path> files = Files.walk(Path.of(IMAGE_DIRECTORY))) {
			files.filter(Files::isRegularFile).forEach(source -> {
				String targetPath = IMAGE_DIRECTORY_BIN + source.getFileName();
				syncFileToBin(source.toString(), targetPath);
			});
		} catch (IOException e) {
			System.err.println("전체 파일 동기화 중 오류 발생: " + e.getMessage());
		}
	}

	/**
	 * Google Places API 요청 URL을 생성.
	 * 
	 * @param keyword       검색 키워드
	 * @param nextPageToken 다음 페이지 토큰
	 * @return 생성된 요청 URL
	 */
	private String buildPhotoUrl(String photoReference) {
		return String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=300&photoreference=%s&key=%s",
				photoReference, API_KEY);
	}

	/**
	 * 사진이 변경되었는지 확인하고 필요한 경우에만 업데이트.
	 * 
	 * @param photoReference 새로 가져온 사진 참조 키
	 * @param placeId        음식점 ID
	 */
	private void updatePhotoIfChanged(String photoReference, String placeId) {
		String filePath = IMAGE_DIRECTORY + placeId + ".jpg";
		File file = new File(filePath);

		// 파일이 존재하면 photoReference 변경 여부 확인
		if (file.exists()) {
			// System.out.println("이미 존재하는 이미지: " + filePath);
			return; // 파일이 이미 존재하므로 업데이트하지 않음
		}

		// 파일이 없으면 새로 저장
		savePhoto(photoReference, placeId);
	}

	/**
	 * 사진 URL을 생성.
	 * 
	 * @param photoReference Google Places API 사진 참조 키
	 * @return 생성된 사진 URL
	 */
	private String buildUrl(String keyword, String nextPageToken) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
				.queryParam("location", "35.159707,129.060186").queryParam("radius", DEFAULT_RADIUS)
				.queryParam("type", "restaurant").queryParam("language", LANGUAGE).queryParam("fields", FIELDS)
				.queryParam("key", API_KEY);

		if (keyword != null) {
			builder.queryParam("keyword", URLEncoder.encode(keyword, StandardCharsets.UTF_8));
		}
		if (nextPageToken != null) {
			builder.queryParam("pagetoken", nextPageToken);
		}

		String generatedUrl = builder.toUriString();

		// 디버깅용 콘솔 출력
		System.out.println("생성된 API 요청 URL: " + generatedUrl);

		return generatedUrl;
	}

	/**
	 * 두 좌표 간 거리를 계산.
	 * 
	 * @param lat1 첫 번째 위도
	 * @param lon1 첫 번째 경도
	 * @param lat2 두 번째 위도
	 * @param lon2 두 번째 경도
	 * @return 계산된 거리 (미터 단위)
	 */
	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		final int EARTH_RADIUS = 6371000; // 지구 반지름 (단위: m)
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS * c; // 거리 반환 (미터 단위)
	}

	/**
	 * 페이지 데이터 클래스.
	 * 
	 * 이 클래스는 Google Places API에서 가져온 음식점 리스트와 다음 페이지 요청을 위한 토큰 정보를 담기 위해 사용됩니다.
	 * 
	 * 내부 클래스로 작성된 이유: - PageData는 GoogleNearByPlaceApi 내부에서만 사용되며, 외부에 노출될 필요가
	 * 없습니다. - 내부 클래스로 작성하여 관련 코드 간 결합도를 높이고, 불필요한 노출을 방지합니다.
	 */
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
