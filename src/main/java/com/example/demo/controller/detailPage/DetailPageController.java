package com.example.demo.controller.detailPage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class DetailPageController {
    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    @GetMapping("/detail")
    public String detail(/*@PathVariable("placeId") String placeId */ Model model) {
        // ChIJMcasLGnraDUR3an68Jt0eEs 한옥마을 id, 오픈시간 참고용
        // TODO 테스트용 위치 ID. 추후 지울것
        String placeId = "ChIJX0sLfm7raDURBSWF14n0VD0";
        String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeId +
                "&language=ko&fields=formatted_address,name,rating,geometry,photo,reviews,opening_hours&key=" + GOOGLE_API_KEY;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        model.addAttribute("response", response);
        model.addAttribute("placeId", placeId);
        return "TestMap";
    }
}
