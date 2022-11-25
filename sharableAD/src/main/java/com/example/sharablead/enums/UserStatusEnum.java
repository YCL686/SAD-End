package com.example.sharablead.enums;

import lombok.Getter;

/**
 * @Author YCL686
 * @Date 2022/10/14
 */
@Getter
public enum UserStatusEnum {
    NORMAL(0, "normal"),
    BAN(1, "ban");

    private final int code;
    private final String name;

    UserStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (UserStatusEnum c : UserStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
