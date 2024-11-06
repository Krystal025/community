package com.practice.community.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    REQUIRED_FIELD_MISSING(400, "Missing Required Field"),
    UNAUTHORIZED_ACCESS(403, "Access Denied"),
    USER_NOT_FOUND(404, "User Not Found"),
    POST_NOT_FOUND(404, "Post Not Found"),
    EMAIL_ALREADY_EXISTS(409, "Email Already Exists"),
    NICKNAME_ALREADY_EXISTS(409, "Nickname Already Exists"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int status;
    private final String message;
}
