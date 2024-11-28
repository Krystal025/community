package com.practice.community.exception.custom;

import com.practice.community.exception.ErrorCode;

public class PostNotFoundException extends RuntimeException{

    public PostNotFoundException(ErrorCode errorCode){
        super(errorCode.getMessage());

    }
}
