package com.practice.community.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String mainPage(){

        return "Main Controller";
    }

//    @GetMapping("/login")
//    public String login() {
//        // 로그인 페이지로 이동 (Spring Security가 자동으로 이 경로로 리디렉션 시킴)
//        return "login"; // 별도의 템플릿 없이 Spring Security가 제공하는 기본 로그인 페이지 사용 가능
//    }
//
//    @GetMapping("/oauth2/callback")
//    public String oauth2Callback(@RequestParam String code) {
//        // code를 사용하여 액세스 토큰을 발급받고, 사용자 정보를 처리하는 로직을 구현
//        return "redirect:/home"; // 로그인 후 리디렉션할 페이지
//    }

    @GetMapping("/home")
    public ResponseEntity<String> home() {
        String message = "로그인 성공!";
        System.out.println(message); // 콘솔에 메시지 출력
        return ResponseEntity.ok(message); // REST API 응답으로 메시지 전송
    }

}
