package com.kr.jikim.jwt.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String username;
    private String role;
    private String jikim;
}
