package com.kr.jikim.jwt.controller;

import java.util.logging.Logger;

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
    @PostMapping("/join")
    public String join(@RequestBody JoinDTO joinDTO) {
        logger.info("회원가입 요청");
        logger.info(joinDTO.toString());
        try {
            joinService.join(joinDTO);
        } catch (RuntimeException e) {
            return "이미 가입된 회원입니다.";
        }
        return "회원가입 완료";
    }
}
