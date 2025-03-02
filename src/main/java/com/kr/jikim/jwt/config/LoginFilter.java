package com.kr.jikim.jwt.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kr.jikim.jwt.dto.LoginDTO;
import com.kr.jikim.jwt.entity.RefreshEntity;
import com.kr.jikim.jwt.repository.RefreshRepository;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kr.jikim.jwt.dto.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StreamUtils;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AuthenticationManager authenticationManager;
    private final JWTUtil12 jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

                /* JSON 형식으로 데이터를 받아오는 경우 */
                  LoginDTO loginDTO = new LoginDTO();
                  try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        ServletInputStream inputStream = request.getInputStream();
                        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                        loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                        String username = loginDTO.getUsername();
                        String password = loginDTO.getPassword();


//                String username = obtainUsername(request);
//                String password = obtainPassword(request);
//                String role = request.getParameter("role");
                logger.info("username: " + username);
                logger.info("password: " + password);
//                logger.info("role: " + role);
                // List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                // authorities.add(new SimpleGrantedAuthority(role));

                UsernamePasswordAuthenticationToken authenticationToken = 
                    // new UsernamePasswordAuthenticationToken(username, password, authorities);
                    new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
                logger.info("로그인 성공");
                logger.info("authResult: " + authResult);
                CustomUserDetails customUserDetails = (CustomUserDetails)authResult.getPrincipal();
                Collection<? extends GrantedAuthority> collection = customUserDetails.getAuthorities();
                Iterator<? extends GrantedAuthority> it = collection.iterator();
                String username = customUserDetails.getUsername();
                // String role = customUserDetails.getAuthorities().iterator().next().getAuthority();
                String role = it.next().getAuthority();
                logger.info("username: " + username);
                logger.info("role: " + role);
                /*
                * token 1개 발급
                *
                String token = jwtUtil.createJwt(username, role, 60 * 60 * 60 * 10L);
                logger.info("token: " + token);
                response.addHeader("Authorization", "Bearer " + token);
                */

                /*
                * access, refresh 토큰 2개 생성
                * 1. 헤더에 access 발급
                * 2. 쿠키에 refresh 발급
                * */
                //토큰 생성
                String access = jwtUtil.createJwt("access", username, role, 600000L);  //10분 생명주기를 다르게 access는 짧게
                String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);  // 24시간 refresh 는 길게

                //토큰 저장
                //Refresh 토큰 저장
                addRefreshEntity(username, refresh, 86400000L);

                //응답 설정
                response.setHeader("access", access);
                response.addCookie(cookieUtil.createCookie("refresh", refresh));
                response.setStatus(HttpStatus.OK.value());



                // response.setContentType("application/json");
                // response.setCharacterEncoding("UTF-8");
                // response.getWriter().write("{\"token\":\"" + token + "\"}");
        // TODO Auto-generated method stub
        // super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
                logger.info("로그인 실패");
                logger.info("failed: " + failed);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // response.setContentType("application/json");
                // response.setCharacterEncoding("UTF-8");
                // response.getWriter().write("{\"message\":\"로그인 실패\"}");
        // TODO Auto-generated method stub
        // super.unsuccessfulAuthentication(request, response, failed);
    }

//    private Cookie createCookie(String key, String value) {
//
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(24*60*60);
//        //cookie.setSecure(true);  https 시
//        //cookie.setPath("/");
//        cookie.setHttpOnly(true);  //javascript 로 접근하지 못하도록
//
//        return cookie;
//    }

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