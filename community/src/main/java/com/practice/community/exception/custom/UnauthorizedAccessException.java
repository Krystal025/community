package com.practice.community.exception.custom;

import com.practice.community.exception.ErrorCode;

public class UnauthorizedAccessException extends RuntimeException{

    private final ErrorCode errorCode;

    public UnauthorizedAccessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;

    }
}
