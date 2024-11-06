package com.practice.community.exception.custom;

import com.practice.community.exception.ErrorCode;

public class PostNotFoundException extends RuntimeException{

    private final ErrorCode errorCode;

    public PostNotFoundException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;

    }
}
