package com.example.demo.model;

import lombok.Data;

@Data
//11.15 작성 - 경 
public class Restaurant {
	   private String name;
	    private String address;
	    private double rating;
	    private double distance;
	    private String photoUrl;
	    private double latitude; // 추가
	    private double longitude; // 추가
}
