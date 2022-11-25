package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum WithdrawRequestStatusEnum {
    PENDING(0, "pending"),
    SUCCESS(1, "user"),
    FAIL(2, "fail");

    private final int code;
    private final String name;

    WithdrawRequestStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (WithdrawRequestStatusEnum c : WithdrawRequestStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
