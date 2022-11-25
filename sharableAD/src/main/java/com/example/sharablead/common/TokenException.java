package com.example.sharablead.common;

import com.example.sharablead.enums.GlobalResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenException extends RuntimeException {

    private int code;

    private String msg;

    public TokenException(GlobalResponseEnum globalResponseEnum) {
        this.code = globalResponseEnum.getCode();
        this.msg = globalResponseEnum.getMessage();
    }
}
