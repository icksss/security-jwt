package com.kr.jikim.jwt.interceptor;

import com.kr.jikim.jwt.common.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/*
* supports 메소드는 현재 요청이 beforeBodyWrite에 의해 처리될 요청이 맞는지 조건문을 정의해주는 메소드인데 우리는 모든 요청에 대해서 적용할 예정이므로 그냥 true를 적어주었다.
* beforeBodyWrite 메소드는 컨트롤러 메소드가 반환한 객체를 최종 응답으로 클라이언트에 전송하기 전 가로채어 필요한 변형이나 추가 처리를 수행하는 메소드이다. 응답이 ApiResponse class를 반환하는 경우 내부의 httpStatus를 받아와서(아까 @JsonIgnore가 적용돼있던 값, 지금까지 계속 넣어주던 HttpStatus 값) 내보낼 응답의 statusCode를 해당 값으로 바꿔준다.
* 이후에 body를 반환함을 통해서 원래 의도했던 HttpStatus 값을 ResponseHeader의 status code로 사용할 수 있다.
*
* */

@RestControllerAdvice
public class ResponseInterceptor implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (returnType.getParameterType() == ApiResponse.class) {
            HttpStatus status = ((ApiResponse<?>) body).httpStatus();
            response.setStatusCode(status);
        }

        return body;
    }
}
