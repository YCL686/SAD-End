package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum AdAuctionRecordStatusEnum {
    ONGOING(0, "ongoing"),
    EXCEED(1, "exceed"),
    SUCCESS(2, "success");

    private final int code;
    private final String name;

    AdAuctionRecordStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (AdAuctionRecordStatusEnum c : AdAuctionRecordStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
