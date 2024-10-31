package com.practice.community.user.controller;

import com.practice.community.user.dto.UserDto;
import com.practice.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 사용자 등록 API
    @PostMapping
    public ResponseEntity<String> saveUser(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered");
    }

    // 사용자 목록 조회 API
    @GetMapping("/list")
    public List<UserDto> getList(){
        return userService.getList();
    }



}
