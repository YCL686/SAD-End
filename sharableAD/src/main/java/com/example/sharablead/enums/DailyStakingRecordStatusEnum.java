package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum DailyStakingRecordStatusEnum {
    UNSETTLE(0, "unsettle"),
    SETTLED(1, "settled");

    private final int code;
    private final String name;

    DailyStakingRecordStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (DailyStakingRecordStatusEnum c : DailyStakingRecordStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
