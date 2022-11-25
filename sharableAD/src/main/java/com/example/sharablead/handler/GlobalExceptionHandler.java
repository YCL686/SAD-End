package com.example.sharablead.handler;

import com.example.sharablead.common.GlobalException;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.common.TokenException;
import com.example.sharablead.enums.GlobalResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({GlobalException.class})
    public GlobalResponse exceptionHandler(GlobalException exception){
        log.error(exception.getMsg());
// 省略记录日志
        return new GlobalResponse(exception.getCode(), exception.getMsg(), null);
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public GlobalResponse exceptionHandler(Exception exception){
// 省略记录日志
        log.error(exception.getMessage());
        return new GlobalResponse(GlobalResponseEnum.ERROR.getCode(), GlobalResponseEnum.UNKNOWN_ERROR.getMessage(), null);
    }

    @ResponseBody
    @ExceptionHandler({TokenException.class})
    public GlobalResponse exceptionHandler(TokenException tokenException){
// 省略记录日志
        log.error(tokenException.getMessage());
        return new GlobalResponse(GlobalResponseEnum.TOKEN_ERROR.getCode(), tokenException.getMsg(), null);
    }

}