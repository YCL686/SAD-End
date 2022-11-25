package com.example.sharablead.config;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import springfox.documentation.service.Header;

@ControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    ObjectMapper objectMapper;

    //判断是否要执行beforeBodyWrite方法,true为执行,false不执行. 通过该方法可以选择哪些类或那些方法的response要进行处理, 其他的不进行处理.
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    //对response方法进行具体操作处理
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //TODO 放开一些response返回 不处理

        //返回类型是否已经封装
        if (body instanceof GlobalResponse){
            return body;
        }
        // String特殊处理
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(GlobalResponse.success(body));
            } catch (JsonProcessingException e) {
                return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), GlobalResponseEnum.ERROR.getMessage());
            }
        }
        return GlobalResponse.success(body);
    }
}