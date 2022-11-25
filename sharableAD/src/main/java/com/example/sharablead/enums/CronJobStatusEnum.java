package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum CronJobStatusEnum {

    ONLINE(0, "online"),
    OFFLINE(1, "offline");

    private final int code;
    private final String name;

    CronJobStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (CronJobStatusEnum c : CronJobStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
