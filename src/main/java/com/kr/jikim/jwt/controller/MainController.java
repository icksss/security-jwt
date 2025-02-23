package com.kr.jikim.jwt.controller;

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kr.jikim.jwt.dto.CustomUserDetails;

@Controller
@ResponseBody
public class MainController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/")

    
    public String mainp() {
        logger.info("mainController");
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("name: " + name);
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        logger.info("role: " + role);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("authentication: " + authentication);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        logger.info("authorities: " + authorities);
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        logger.info("auth: " + ((CustomUserDetails)authentication.getPrincipal()).getJikim());
        // CustomUserDetails customUserDetails = (CustomUserDetails)authResult.getPrincipal();
        return "mainController";
    }
}
