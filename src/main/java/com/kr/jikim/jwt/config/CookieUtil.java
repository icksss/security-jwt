package com.kr.jikim.jwt.config;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);  https 시
        //cookie.setPath("/");
        cookie.setHttpOnly(true);  //javascript 로 접근하지 못하도록

        return cookie;
    }
}
