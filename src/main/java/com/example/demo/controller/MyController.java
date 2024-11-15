package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class MyController {

    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/test")
    public String test() {
        return "TestMap";
    }

    @GetMapping("/getPlaceDetails")
    public @ResponseBody ResponseEntity<String> getPlaceDetails(@RequestParam String placeId) {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeId +
                "&language=ko&fields=formatted_address,name,rating,geometry,photo,reviews,opening_hours&key=" + GOOGLE_API_KEY;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/1")
    public String index1() {
        return "index1";
    }
    @GetMapping("/2")
    public String index2() {
        return "index2";
    }
    @GetMapping("/3")
    public String index3() {
        return "index3";
    }
}
