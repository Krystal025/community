package com.practice.community.exception;

import com.practice.community.exception.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 프로젝트 전체의 예외를 한곳에서 처리함
public class GlobalExceptionHandler {

    // MethodArgumentsNotValid : 유효성 검사(@Valid)가 실패했을 때 발생하는 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> exceptionHandler(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        // 유효성 검사 실패한 필드의 에러 메시지 처리
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequiredFieldMissingException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(RequiredFieldMissingException ex){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.REQUIRED_FIELD_MISSING);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(UnauthorizedAccessException ex){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.UNAUTHORIZED_ACCESS);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(UserNotFoundException ex){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.USER_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(PostNotFoundException ex){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.POST_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(EmailAlreadyExistsException ex){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.EMAIL_ALREADY_EXISTS);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(NicknameAlreadyExistsException ex){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NICKNAME_ALREADY_EXISTS);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedOAuth2Provider.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(UnsupportedOAuth2Provider ex){
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.UNSUPPORTED_OAUTH2_PROVIDER);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
