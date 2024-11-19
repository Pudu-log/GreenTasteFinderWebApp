package com.example.demo.controller.mainPage;

import com.example.demo.model.Restaurant;
import com.example.demo.service.MainPageService;
import com.example.demo.utils.GoogleNearByPlaceApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainPageApiController {

	private final MainPageService mainPageService;
	private final GoogleNearByPlaceApi googleNearByPlaceApi;

	@Autowired
	public MainPageApiController(MainPageService mainPageService, GoogleNearByPlaceApi googleNearByPlaceApi) {
		this.mainPageService = mainPageService;
		this.googleNearByPlaceApi = googleNearByPlaceApi;
	}

    @GetMapping("/page/{pageNumber}")
    public List<Restaurant> getNearbyRestaurants(@RequestParam(name = "sortBy", defaultValue = "rating") String sortBy,
                                                  @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber) {
        try {
            // GoogleNearByPlaceApi를 사용해 데이터 가져오기 및 정렬
            List<Restaurant> allRestaurants = googleNearByPlaceApi.fetchAllNearbyRestaurants(null, sortBy);
            // 페이징 처리
            return getPagedRestaurants(allRestaurants, pageNumber, 12);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("서버 내부 오류가 발생했습니다.", e);
        }
    }

    private List<Restaurant> getPagedRestaurants(List<Restaurant> allRestaurants, int pageNumber, int size) {
        int totalRestaurants = allRestaurants.size();
        int startIdx = (pageNumber - 1) * size;
        int endIdx = Math.min(startIdx + size, totalRestaurants);

        if (startIdx >= totalRestaurants) {
            return List.of();
        }

        // 정렬된 데이터에서 페이징된 부분만 반환
        return allRestaurants.subList(startIdx, endIdx);
    }
}
