package com.practice.community.exception.custom;

import com.practice.community.exception.ErrorCode;

public class NicknameAlreadyExistsException extends RuntimeException{

    public NicknameAlreadyExistsException(ErrorCode errorCode){
        super(errorCode.getMessage());
    }
}
