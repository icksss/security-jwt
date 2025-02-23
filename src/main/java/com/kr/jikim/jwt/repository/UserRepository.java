package com.kr.jikim.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kr.jikim.jwt.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);
    boolean existsByUsername(String username);
}
