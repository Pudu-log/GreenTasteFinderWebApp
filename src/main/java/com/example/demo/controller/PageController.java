package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class PageController {
    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    @GetMapping("/test")
    public String test(/*@PathVariable("placeId") String placeId */ Model model) {
        String placeId = "ChIJ70lL5f4iZDURou4DxhPonPA";
        String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeId +
                "&language=ko&fields=formatted_address,name,rating,geometry,photo,reviews,opening_hours&key=" + GOOGLE_API_KEY;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        model.addAttribute("response", response);
        return "TestMap";
    }
}