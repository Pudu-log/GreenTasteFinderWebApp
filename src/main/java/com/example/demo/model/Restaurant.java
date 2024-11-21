package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
/* 
작성자: 구경림
작성일: 2024.11.20
작성이유: 
*/
@Getter
@Setter
@ToString
public class Restaurant {
    private String placeId;           // 장소 ID
    private String name;              // 레스토랑 이름
    private String address;           // 주소
    private String phoneNumber;       // 전화번호
    private String website;           // 웹사이트 URL
    private String photoUrl;          // 사진 URL
    private List<String> openingHours; // 영업 시간 (목록 형태)
    private boolean openNow = false;  // 현재 영업 중인지 여부
    private double rating = 0.0;      // 평점
    private int reviewCount = 0;      // 리뷰 수
    private int priceLevel = -1;      // 가격 수준
    private double distance = 0.0;    // 거리 (사용자와의 거리)
    private boolean liked = false;    // 좋아요 여부
    private boolean favorited = false; // 즐겨찾기 여부
}
