package com.example.sharablead.enums;

import lombok.Getter;

/**
 * @Author YCL686
 * @Date 2022/11/4
 */
@Getter
public enum DailyTaskConfigStatusEnum {
    ONLINE(0, "online"),
    OFFLINE(1, "offline");

    private final int code;
    private final String name;

    DailyTaskConfigStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (DailyTaskConfigStatusEnum c : DailyTaskConfigStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
