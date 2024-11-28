package com.practice.community.exception.custom;

import com.practice.community.exception.ErrorCode;

public class UnauthorizedAccessException extends RuntimeException{

    public UnauthorizedAccessException(ErrorCode errorCode){
        super(errorCode.getMessage());

    }
}
