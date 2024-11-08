package com.practice.community.exception.custom;

import com.practice.community.exception.ErrorCode;

public class EmailAlreadyExistsException extends RuntimeException{

    private final ErrorCode errorCode;

    // 생성자에서 errorCode의 메시지를 부모 클래스에 전달함
    public EmailAlreadyExistsException(ErrorCode errorCode){
        super(errorCode.getMessage()); // 메시지를 부모 클래스의 생성자에 전달함
        this.errorCode = errorCode; // ErrorCode 객체를 저장함
    }
}