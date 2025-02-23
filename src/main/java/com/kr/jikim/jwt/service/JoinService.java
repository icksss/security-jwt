package com.kr.jikim.jwt.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kr.jikim.jwt.dto.JoinDTO;
import com.kr.jikim.jwt.entity.UserEntity;
import com.kr.jikim.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(JoinDTO joinDTO) throws RuntimeException{
        if (userRepository.existsByUsername(joinDTO.getUsername())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(joinDTO.getUsername());
        userEntity.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        userEntity.setRole(joinDTO.getRole());
        userRepository.save(userEntity);
    }
}