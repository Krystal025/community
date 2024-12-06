package com.practice.community.controller;

import com.practice.community.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(value = "Refresh_Token", required = false) String refreshToken){
        if(refreshToken == null || jwtTokenProvider.isExpired(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token Expired");
        }
        String newAccessToken = jwtTokenProvider.refreshAccessToken(refreshToken);
        System.out.println("Refresh Access Token");
        return ResponseEntity.ok().header("Authorization", "Bearer " + newAccessToken).build();
    }
}
