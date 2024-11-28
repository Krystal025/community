package com.practice.community.exception.custom;

import com.practice.community.exception.ErrorCode;

public class RequiredFieldMissingException extends RuntimeException{

    public RequiredFieldMissingException(ErrorCode errorCode){
        super(errorCode.getMessage());

    }
}
