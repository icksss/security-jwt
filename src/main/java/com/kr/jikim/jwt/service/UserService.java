package com.kr.jikim.jwt.service;

import com.kr.jikim.jwt.entity.UserEntity;
import com.kr.jikim.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserEntity> getUsers() throws Exception{
        return userRepository.findAll();
    }
}
