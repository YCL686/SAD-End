package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum AvatarNFTRarityEnum {
    COPPER(0, "copper"),
    SILVER(1, "silver"),
    GOLD(2, "gold");

    private final int code;
    private final String name;

    AvatarNFTRarityEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(int code) {
        for (AvatarNFTRarityEnum c : AvatarNFTRarityEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
