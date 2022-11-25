package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum LikeStatusEnum {

    NORMAL(0, "normal"),
    CANCEL(1, "cancel");

    private final int code;
    private final String name;

    LikeStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (LikeStatusEnum c : LikeStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }

}
