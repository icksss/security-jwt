package com.kr.jikim.jwt.controller;

import com.kr.jikim.jwt.common.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class AdminController {
    @GetMapping("/admin")
    public ApiResponse<?> adminP() {
        return ApiResponse.ok("adminController");
    }

    @GetMapping("/api/admin")
    public ApiResponse<?> successWithMessage() {
        return ApiResponse.ok("ok");
    }
}
