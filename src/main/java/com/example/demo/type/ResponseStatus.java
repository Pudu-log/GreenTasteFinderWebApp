package com.example.demo.type;

import lombok.Getter;

/**
 * 2024-11-15
 * 한우성
 *  API 응답 상태를 정의하는 타입
 * 각 상태는 HTTP 상태 코드와 설명 메시지를 포함
 */
@Getter
public enum ResponseStatus {
    SUCCESS(200, "성공"),
    BAD_REQUEST(400, "잘못된 요청"),
    UNAUTHORIZED(401, "인증되지 않음"),
    FORBIDDEN(403, "금지됨"),
    NOT_FOUND(404, "찾을 수 없음"),
    INTERNAL_ERROR(500, "서버 내부 오류");

    private final int code;
    private final String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

}