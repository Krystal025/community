package com.practice.community.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static void addCookie(HttpServletResponse response, String name, String value){
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일 유효
        response.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest request, String name){
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(name.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
