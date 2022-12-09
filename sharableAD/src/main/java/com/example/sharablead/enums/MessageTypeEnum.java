package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum MessageTypeEnum {

    GET_LIKE(0, "get like"),
    GET_COMMENT(1, "get comment"),
    GET_FOCUS(2,"get focus"),
    GET_REWARD(3, "get reward"),
    GET_DAILY_TASK(4,"get dailyTask"),
    GET_DAILY_STAKING(5, "get dailyStaking"),
    GET_NFT(6, "get nft"),
    GET_AUCTION(7,"get auction"),
    GET_LAUNCH(8,"get launch"),
    GET_WITHDRAW(9, "get withdraw");
    //TODO ...


    private final int code;
    private final String name;

    MessageTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (MessageTypeEnum c : MessageTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
