package com.kr.jikim.jwt.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kr.jikim.jwt.dto.CustomUserDetails;
import com.kr.jikim.jwt.entity.UserEntity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTFiler extends OncePerRequestFilter{
    private final JWTUtil12 jwtUtil;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        logger.info("JWTFiler token: " + token);
        
        if(token == null || !token.startsWith("Bearer ")){
            logger.info("JWTFiler token is null or not starts with Bearer");
            filterChain.doFilter(request, response);
            return;
        }
        token = token.split(" ")[1];
        logger.info("JWTFiler token Bearer 제거 후 : " + token);
        if(jwtUtil.isExpired(token)){
            logger.info("JWTFiler token is expired");
            filterChain.doFilter(request, response);
            return;
        }
        
        
        
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        logger.info("JWTFiler username: " + username);
        logger.info("JWTFiler role: " + role);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("tmp");
        userEntity.setRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }
    
}
