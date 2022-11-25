package com.example.sharablead.enums;

import lombok.Getter;

/**
 * @Author YCL686
 * @Date 2022/10/14
 */
@Getter
public enum  AccountEntryStatusEnum {
    SUCCESS(0, "success"),
    FAIL(1, "fail"),
    PENDING(2,"pending");

    private final int code;
    private final String name;

    AccountEntryStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(int code) {
        for (AccountEntryStatusEnum c : AccountEntryStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
