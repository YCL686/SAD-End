package com.example.sharablead.common;

import com.example.sharablead.enums.GlobalResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException{

    private int code;

    private String msg;

    public GlobalException(GlobalResponseEnum globalResponseEnum) {
        this.code = globalResponseEnum.getCode();
        this.msg = globalResponseEnum.getMessage();
    }
}