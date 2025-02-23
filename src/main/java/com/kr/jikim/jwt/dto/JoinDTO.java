package com.kr.jikim.jwt.dto;

import lombok.Data;

@Data
public class JoinDTO {
    private String username;
    private String password;
    private String role;
}
