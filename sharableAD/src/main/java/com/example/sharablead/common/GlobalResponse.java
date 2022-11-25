package com.example.sharablead.common;

import com.example.sharablead.enums.GlobalResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponse implements Serializable {

    private Integer code;
    private String message;
    private Object data;

    /**
     * 成功时返回的方法（data不为空）
     */
    public static GlobalResponse success(Object o) {
        return new GlobalResponse(GlobalResponseEnum.SUCCESS.getCode(), GlobalResponseEnum.SUCCESS.getMessage(), o);
    }
    /**
     * 成功时返回的方法（data为空）
     */
    public static GlobalResponse success() {
        return success(null);
    }
    /**
     * 失败时返回的方法
     */
    public static GlobalResponse error(Integer code, String message) {
        return new GlobalResponse(code, message,null);
    }

}
