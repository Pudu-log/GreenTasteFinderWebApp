package com.example.demo.service;

import com.example.demo.model.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MainPageService {

    @Value("${google.api.key}")
    private String apiKey;

    private static final String PLACES_API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public MainPageService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Restaurant> getNearbyRestaurants(double latitude, double longitude) {
        List<Restaurant> restaurants = new ArrayList<>();
        String url = String.format("%s?location=%f,%f&radius=1000&type=restaurant&key=%s",
                PLACES_API_URL, latitude, longitude, apiKey);

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.path("results");

            for (JsonNode node : results) {
                Restaurant restaurant = parseRestaurantFromJson(node);
                double restaurantLat = node.path("geometry").path("location").path("lat").asDouble();
                double restaurantLng = node.path("geometry").path("location").path("lng").asDouble();
                double distance = calculateDistance(latitude, longitude, restaurantLat, restaurantLng);
                restaurant.setDistance(distance);
                restaurants.add(restaurant);
            }

            restaurants.sort(Comparator.comparingDouble(Restaurant::getDistance));
            // 별점 정렬은 여전히 활성화되지 않음
            // restaurants.sort(Comparator.comparingDouble(Restaurant::getRating).reversed());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    private Restaurant parseRestaurantFromJson(JsonNode node) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(node.path("name").asText());
        restaurant.setRating(node.path("rating").asDouble());
        restaurant.setAddress(node.path("vicinity").asText());

        if (node.has("photos")) {
            String photoReference = node.path("photos").get(0).path("photo_reference").asText();
            String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s",
                    photoReference, apiKey);
            restaurant.setPhotoUrl(photoUrl);
        }

        restaurant.setDistance(1.0);
        return restaurant;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
