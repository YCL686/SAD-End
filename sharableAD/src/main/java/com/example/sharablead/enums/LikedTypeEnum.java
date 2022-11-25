package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum LikedTypeEnum {
    OPUS(0, "opus"),
    COMMENT(1, "comment");

    private final int code;
    private final String name;

    LikedTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (LikedTypeEnum c : LikedTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
