package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum AuctionTypeEnum {

    BID_NOW(0, "Bid Now"),
    BUY_IT_NOW(1, "Buy It Now");

    private final int code;
    private final String name;

    AuctionTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(int code) {
        for (AuctionTypeEnum c : AuctionTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
