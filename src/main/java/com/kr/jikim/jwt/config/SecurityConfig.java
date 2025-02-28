package com.kr.jikim.jwt.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil12 jwtUtil;
    private final CookieUtil cookieUtil;
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //cors 설정 
        /**
         * 1. Spring filter 처리해야 token 이 전달 된다.(안하면 로그인필터같은 놈들이 문제가됨)
         * 2. spring security filter 와 Controller 레벨에서 Configuration WebMvcConfigurer 를 구현 해야 한다.
         * 둘다 되야 정확히 처리된다.
         */
        http
            .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                }
            })));

        //csrf 비활성화
        http
            .csrf((auth) -> auth.disable());
        //formLogin 비활성화
        http
            .formLogin((auth) -> auth.disable());
        //http basic 비활성화
        http
            .httpBasic((auth) -> auth.disable());
        //경로별 인가 작업
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/", "/join","/reissue").permitAll()  //reissue refresh를 재발급하는 Url
                .requestMatchers("/user").hasRole("USER")
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated());
        //로그인 필터 추가
        /**
         * loginFilter 추가 는 UsernamePasswordAuthenticationFilter 를 상속받은 CustomFilter 이다.
         * 세션방식으로 작업하는 것이 아니라 토큰방식으로 작업하기 위해서 이다.
         * UsernamePasswordAuthenticationFilter 이후 -> AuthenticationManager 에 전달되어 인증 작업을 한후
         * UserDetails -> UserDetailsService -> UserEntity -> UserRepository 에서 조회된 객체가 전달된다.( d이부분은 세션방식과 같다.)
         * AuthenticationManager 에서 인증 작업을 한후 -> Authentication 객체가 전달된다.
         * 성공인경우 -> successfulAuthentication 메서드가 호출된다.
         * 실패인경우 -> unsuccessfulAuthentication 메서드가 호출된다.
         * 성공인 경우 JWTUtils 에서 토큰을 생성하고 토큰을 응답한다.
         * 실패인 경우 401 에러를 응답한다.
         * 
         */
        http
            .addFilterBefore(new JWTFiler(jwtUtil), LoginFilter.class);
        http
            .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil,cookieUtil), UsernamePasswordAuthenticationFilter.class);
        //세션설정(SessionCreationPolicy.STATELESS 서버가 세션정보를 계속 가지고 있지 않음.)
        http
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            
        return http.build();
    }
        
        
}
