package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum SynchronizeTypeEnum {
    REWARD(0, "Reward"),
    AD_AUCTION(1, "AdAuction"),
    DAILY_TASK(2, "DailyTask"),
    DAILY_STAKING(3, "DailyStaking"),
    NFT(4, "NFT");
    //TODO more type to be continue...

    private final int code;
    private final String name;

    SynchronizeTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (SynchronizeTypeEnum c : SynchronizeTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
