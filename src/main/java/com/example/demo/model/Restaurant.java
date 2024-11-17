package com.example.demo.model;

import java.util.List;

import lombok.Data;

@Data
public class Restaurant {
	   private String name;
	    private String address;
	    private double rating;
	    private double distance;
	    private String photoUrl;
	    private String placeId; // placeId 
	    private String phoneNumber; // 전화번호
	    private List<String> openingHours; // 영업시간
	    private Boolean openNow; // 현재 영업 중인지 여부
	    private int reviewCount;
	    private boolean liked = false; // 좋아요 여부 기본값
	    private boolean favorited = false; // 즐겨찾기 여부 기본값
}
