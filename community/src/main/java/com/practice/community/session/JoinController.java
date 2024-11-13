package com.practice.community.session;

import com.practice.community.user.dto.UserRequestDto;
import com.practice.community.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;

    @GetMapping("/join")
    public String joinPage(){
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(@Valid @RequestBody UserRequestDto userRequestDto){
        System.out.println("Received password: " + userRequestDto.getUserPwd());
        userService.saveUser(userRequestDto);
        return "redirect:/login";
    }
}
