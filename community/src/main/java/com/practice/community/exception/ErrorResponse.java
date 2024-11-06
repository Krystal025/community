package com.practice.community.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse { // 예외 응답을 위한 엔티티

    private int status; // HTTP 상태 코드
    private String message; // 예외 메시지

    // ErrorCode를 활용해 상태코드와 메시지를 생성
    public static ErrorResponse of(ErrorCode errorCode){
        return new ErrorResponse(errorCode.getStatus(), errorCode.getMessage());
    }
}
