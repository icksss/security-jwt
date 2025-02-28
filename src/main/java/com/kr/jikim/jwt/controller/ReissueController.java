package com.kr.jikim.jwt.controller;

import com.kr.jikim.jwt.config.CookieUtil;
import com.kr.jikim.jwt.config.JWTUtil12;
import com.kr.jikim.jwt.entity.RefreshEntity;
import com.kr.jikim.jwt.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@ResponseBody
public class ReissueController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final JWTUtil12 jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshRepository refreshRepository;

    public ReissueController(JWTUtil12 jwtUtil, CookieUtil cookieUtil,RefreshRepository refreshRepository) {

        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.refreshRepository = refreshRepository;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("ReissueController!!");
        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        log.info("ReissueController!! newAccess : {}", newAccess);
        //refresh rotate ( refresh 토큰으로 access token 발급시 refresh token도 새로 발급
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //새로운 refresh token 이 발급 되었으므로, DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        //refresh token이 탈취 되었을때, 추후 해당 refresh token으로 access token을 발급 받지 못하도록 한다.
        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(username, newRefresh, 86400000L);


        //response
        response.setHeader("access", newAccess);
        //refresh rotate ( refresh 토큰으로 access token 발급시 refresh token도 새로 쿠키로 발급
        response.addCookie(cookieUtil.createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //refresh token DB에 저장
    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}
