package com.example.demo.utils;


import com.example.demo.type.ResponseStatus;
import lombok.Data;

/**
 * 2024-11-15
 * 한우성
 * API 응답의 표준 형식을 정의하는 클래스
 * @param <T> 응답 데이터의 타입
 */
@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public ApiResponse(ResponseStatus status, T data) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = data;
    }

}