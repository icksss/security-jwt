package com.kr.jikim.jwt.controller;

import com.kr.jikim.jwt.common.ApiResponse;
import com.kr.jikim.jwt.dto.UserDTO;
import com.kr.jikim.jwt.entity.UserEntity;
import com.kr.jikim.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;

    @GetMapping("/api/users")
    public ApiResponse<?> getUsers() throws Exception{
        Map<String, Object> res = new HashMap<>();


        List<UserEntity> list = userService.getUsers();
        List<UserDTO> result = new ArrayList<>();
        if(!list.isEmpty()){
             result = list.stream()
                    .map(entitiy -> {
                        UserDTO dto = new UserDTO();
                        dto.setId(entitiy.getId());
                        dto.setRole(entitiy.getRole());
                        dto.setJikim(entitiy.getJikim());
                        dto.setUsername(entitiy.getUsername());
                        log.info("dto : {} ", dto);
                        return dto;
                    })
                    .collect(Collectors.toList());
//            res.put("data", result);
        }
        return ApiResponse.ok(result);
//        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/api/user/me")
    public ApiResponse<?> getMe() throws Exception{
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("name: " + name);
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        log.info("role: " + role);
        Map<String, String> result = new HashMap<>();
        result.put("username", name);
        result.put("role", role);
        return ApiResponse.ok(result);
    }
}
