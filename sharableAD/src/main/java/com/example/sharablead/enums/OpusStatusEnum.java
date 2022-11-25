package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum OpusStatusEnum {
    WAIT_AUDIT(0, "wait_audit"),
    NORMAL(1, "normal"),
    DRAFT(2, "draft"),
    AUDIT_FAILED(3, "audit_failed");

    private final int code;
    private final String name;

    OpusStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (OpusStatusEnum c : OpusStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
