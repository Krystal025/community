package com.practice.community.controller;

import com.practice.community.user.dto.UserResponseDto;
import com.practice.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping
    public String adminPage(){
        return "Admin Page";
    }

    // 사용자 목록 조회 API
    @GetMapping("/user_list")
    public ResponseEntity<List<UserResponseDto>> getUserList(){
        List<UserResponseDto> userList = userService.getList();
        return ResponseEntity.ok(userList); // 200 OK
    }
}
