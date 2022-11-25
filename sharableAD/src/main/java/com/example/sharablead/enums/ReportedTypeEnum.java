package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum ReportedTypeEnum {
    OPUS(0, "opus"),
    COMMENT(1, "comment"),
    USER(2,"user");

    private final int code;
    private final String name;

    ReportedTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (ReportedTypeEnum c : ReportedTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
