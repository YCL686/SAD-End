package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum AdStatusEnum {

    NORMAL(0, "normal"),
    AUDITING(1, "auditing"),
    UNDER_CARRIAGE(2, "undercarriage");

    private final int code;
    private final String name;

    AdStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (AdStatusEnum c : AdStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
