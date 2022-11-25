package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum AdAuctionStatusEnum {

    NOT_START(0, "Waiting"),
    ONGOING(1, "Ongoing"),
    SUCCESS(2, "Success"),
    FLOP(3, "Flop");

    private final int code;
    private final String name;

    AdAuctionStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (AdAuctionStatusEnum c : AdAuctionStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
