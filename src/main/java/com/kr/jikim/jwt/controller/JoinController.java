package com.kr.jikim.jwt.controller;

import java.util.logging.Logger;

import com.kr.jikim.jwt.common.ApiResponse;
import com.kr.jikim.jwt.error.CustomException;
import com.kr.jikim.jwt.error.ErrorCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kr.jikim.jwt.dto.JoinDTO;
import com.kr.jikim.jwt.service.JoinService;

import lombok.RequiredArgsConstructor;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class JoinController {
    private final Logger logger = Logger.getLogger(JoinController.class.getName());
    private final JoinService joinService;
    @PostMapping("/api/join")
    public ApiResponse<?> join(@RequestBody JoinDTO joinDTO) {
        logger.info("회원가입 요청");
        logger.info(joinDTO.toString());
        try {
            joinService.join(joinDTO);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorCode.CONFLICT);
        }
        return ApiResponse.created("created");
    }
}
