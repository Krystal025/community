package com.practice.community.exception.custom;

import com.practice.community.exception.ErrorCode;

public class UserNotFoundException extends RuntimeException{

    // 예외 메시지를 받아 상위 클래스에 전달하는 생성자
    public UserNotFoundException(ErrorCode errorCode){
        super(errorCode.getMessage()); // 부모 클래스인 RuntimeException의 생성자에 메시지를 전달함
    }
}
