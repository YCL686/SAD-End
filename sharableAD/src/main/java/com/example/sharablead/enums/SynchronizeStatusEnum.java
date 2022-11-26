package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum SynchronizeStatusEnum {
    SUCCESS(0, "success"),
    FAIL(1, "fail"),
    PENDING(2, "pending"),
    NULL(3, "-");

    private final int code;
    private final String name;

    SynchronizeStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (SynchronizeStatusEnum c : SynchronizeStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
