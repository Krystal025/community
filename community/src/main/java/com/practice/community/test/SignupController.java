package com.practice.community.test;

import com.practice.community.user.dto.UserRequestDto;
import com.practice.community.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupPage(){
        return "signup";
    }

    @PostMapping("/signupProc")
    public String signupProcess(@Valid @RequestBody UserRequestDto userRequestDto){
        System.out.println("Received password: " + userRequestDto.getUserPwd());
        userService.saveUser(userRequestDto);
        return "redirect:/login";
    }
}
