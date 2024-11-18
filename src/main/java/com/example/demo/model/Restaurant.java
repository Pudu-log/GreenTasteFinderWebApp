package com.example.demo.model;

import java.util.List;

import lombok.Data;

@Data
public class Restaurant {
    private String name;
    private String address;
    private double rating;
    private String photoUrl;
    private String phoneNumber;
    private String openingHours;
    private int priceLevel;
    private String website;
    private double distance;
    private String placeId;
    private int reviewCount; 
    private Boolean liked;
    private boolean favorited;
    private Boolean openNow; 
}
