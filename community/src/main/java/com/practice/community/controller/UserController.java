package com.practice.community.controller;

import com.practice.community.user.dto.UserRequestDto;
import com.practice.community.user.dto.UserResponseDto;
import com.practice.community.user.service.UserService;
import jakarta.validation.Valid;
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

    // 사용자 목록 조회 API
    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDto>> getUserList(){
        List<UserResponseDto> userList = userService.getList();
        return ResponseEntity.ok(userList); // 200 OK
    }

    // 사용자 정보 조회 API
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable("userId") Long userId){
        UserResponseDto userResponseDto = userService.getUser(userId);
        return ResponseEntity.ok(userResponseDto);
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
