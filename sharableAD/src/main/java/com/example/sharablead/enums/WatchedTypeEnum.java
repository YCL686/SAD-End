package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum WatchedTypeEnum {
    OPUS(0, "opus"),
    USER(1, "user");

    private final int code;
    private final String name;

    WatchedTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (WatchedTypeEnum c : WatchedTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
