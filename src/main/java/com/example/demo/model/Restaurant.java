package com.example.demo.model;

import java.util.List;

import lombok.Data;

@Data
public class Restaurant {
    private String placeId;          // 장소 ID
    private String name;             // 레스토랑 이름
    private String address;          // 주소
    private String phoneNumber;      // 전화번호
    private String website;          // 웹사이트 URL
    private String photoUrl;         // 사진 URL
    private String openingHours;     // 영업 시간
    private Boolean openNow;         // 현재 영업 중인지 여부
    private double rating;           // 평점
    private int reviewCount;         // 리뷰 수
    private int priceLevel;          // 가격 수준
    private double distance;         // 거리 (사용자와의 거리)
    private Boolean liked;           // 좋아요 여부
    private boolean favorited;       // 즐겨찾기 여부
}
