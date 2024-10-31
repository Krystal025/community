package com.practice.community.user.controller;

import com.practice.community.user.dto.UserDto;
import com.practice.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> saveUser(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered");
    }



}
