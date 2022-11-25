package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum SynchronizedFlagEnum {
    OUT_OF_SYNCHRONIZED(0, "out of synchronized"),
    SYNCHRONIZED(1, "synchronized");

    private final int code;
    private final String name;

    SynchronizedFlagEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (SynchronizedFlagEnum c : SynchronizedFlagEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
