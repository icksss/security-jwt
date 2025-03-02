package com.kr.jikim.jwt.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kr.jikim.jwt.error.CustomException;
import com.kr.jikim.jwt.error.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

//https://github.com/tkdwns414/FleetingSpring.git
public record ApiResponse<T>(
            @JsonIgnore
            HttpStatus httpStatus,
            boolean success,
            @Nullable T data,
            @Nullable ExceptionDto error
    ) {

        public static <T> ApiResponse<T> ok(@Nullable final T data) {
            return new ApiResponse<>(HttpStatus.OK, true, data, null);
        }

        public static <T> ApiResponse<T> created(@Nullable final T data) {
            return new ApiResponse<>(HttpStatus.CREATED, true, data, null);
        }

        public static <T> ApiResponse<T> fail(final CustomException e) {
            return new ApiResponse<>(e.getErrorCode().getHttpStatus(), false, null, ExceptionDto.of(e.getErrorCode()));
        }
    }

