package com.example.demo.utils;

import com.example.demo.type.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 2024-11-15
 * 한우성
 * 애플리케이션 전역에서 발생하는 예외를 처리하는 글로벌 예외 클래스
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 모든 예외를 처리함
     *
     * @param ex 발생한 예외
     * @return 내부 서버 오류에 대한 API 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(ResponseStatus.INTERNAL_ERROR, ex.getMessage()));
    }

}