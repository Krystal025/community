package com.practice.community.user.controller;

import com.practice.community.user.dto.UserRequestDto;
import com.practice.community.user.dto.UserResponseDto;
import com.practice.community.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> saveUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        userService.saveUser(userRequestDto);
        return ResponseEntity.ok("User registered");
    }

    // 사용자 목록 조회 API
    @GetMapping("/list")
    public List<UserResponseDto> getUserList(){
        return userService.getList();
    }

    // 사용자 정보 조회 API
    @GetMapping("/{userId}")
    public UserResponseDto getUserInfo(@PathVariable("userId") Long userId){
        return userService.getUser(userId);
    }

    // 사용자 정보 수정 API
    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable("userId") Long userId,
                                             @Valid @RequestBody UserRequestDto userRequestDto){
        userService.updateUser(userId, userRequestDto);
        return ResponseEntity.ok("User Information Updated");
    }

    // 사용자 비활성화(탈퇴) API
    @PutMapping("/delete/{userId}")
    public ResponseEntity<String> deactivateUser(@PathVariable("userId") Long userId){
        userService.deactivateUser(userId);
        return ResponseEntity.ok("User Deleted");
    }
}
