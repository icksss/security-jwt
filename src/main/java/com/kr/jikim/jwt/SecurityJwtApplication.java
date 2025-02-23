package com.kr.jikim.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityJwtApplication {

	public static void main(String[] args) { 
		System.out.println("SecurityJwtApplication 실행"); 
		SpringApplication.run(SecurityJwtApplication.class, args);
	}

}
