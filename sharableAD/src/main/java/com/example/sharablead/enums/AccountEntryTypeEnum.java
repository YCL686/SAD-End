package com.example.sharablead.enums;

import lombok.Getter;

/**
 * @Author YCL686
 * @Date 2022/10/14
 */
@Getter
public enum AccountEntryTypeEnum {
    IN(0, "in"),
    OUT(1, "out");

    private final int code;
    private final String name;

    AccountEntryTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(int code) {
        for (AccountEntryTypeEnum c : AccountEntryTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
