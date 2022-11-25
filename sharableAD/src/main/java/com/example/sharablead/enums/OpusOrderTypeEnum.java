package com.example.sharablead.enums;

import lombok.Getter;

/**
 * @Author YCL686
 * @Date 2022/10/15
 */
@Getter
public enum OpusOrderTypeEnum {
    LATEST(0, "latest"),
    HOT(1, "hot"),
    FOCUS(2,"focus"),
    RECOMMEND(3, "recommend");

    private final int code;
    private final String name;

    OpusOrderTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (OpusOrderTypeEnum c : OpusOrderTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
