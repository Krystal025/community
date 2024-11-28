package com.practice.community.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String mainPage(){

        return "Main Controller";
    }

    @GetMapping("/login/oauth2/code/google")
    public String oauth2Callback(@RequestParam String code) {
        // code를 사용해 액세스 토큰을 요청하고 처리하는 로직
        System.out.println("oauth2Callback 실행");
        return "OAuth2 인증 완료!";
    }

}
