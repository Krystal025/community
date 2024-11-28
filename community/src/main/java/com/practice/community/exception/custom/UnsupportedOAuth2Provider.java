package com.practice.community.exception.custom;

import com.practice.community.exception.ErrorCode;

public class UnsupportedOAuth2Provider extends RuntimeException{

    public UnsupportedOAuth2Provider(ErrorCode errorCode){
        super(errorCode.getMessage());

    }
}
