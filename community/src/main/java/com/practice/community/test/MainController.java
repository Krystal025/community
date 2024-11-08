package com.practice.community.test;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(Model model){

        // 현재 인증된(로그인된) 사용자의 이름
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("name", name);

        // 현재 인증된 사용자의 정보 (사용자 아이디, 권한 등)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자가 가지는 권한들을 컬렉션 형태로 변환
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 해당 컬렉션을 순차적으로 탐색할 수 있는 Iterator 객체로 변환
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        // 권한 컬렉션에서 첫번째 권한을 가져옴
        GrantedAuthority auth = iter.next();
        // 사용자의 권한 이름을 문자열로 반환
        String role = auth.getAuthority();
        model.addAttribute("role",role);

        return "main";
    }

}
