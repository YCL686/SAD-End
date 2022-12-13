package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum MessageTypeEnum {

    GET_LIKE(0, "get like"), // {name} liked your opus {title} or your comment {content} {time} ago
    GET_COMMENT(1, "get comment"), // {name} commented your opus {title} or your comment {content} {time} ago
    GET_FOCUS(2,"get focus"), // {name} focused you {time} ago
    GET_COLLECT(3, "get collect"), // {name} collected your opus {title} {time} ago
    GET_REWARD(4, "get reward"), // {name} rewarded you {amount} $SAD {time} ago
    //above C2C
    GET_DAILY_TASK(5,"get dailyTask"), //you finished {taskName} and got {amount} $SAD {time} ago
    GET_DAILY_STAKING(6, "get dailyStaking"), // you
    GET_AUCTION(7,"get auction"), //you succeed auction or
    GET_LAUNCH(8,"get launch"), // you launch succeed
    GET_WITHDRAW(9, "get withdraw"), //withdraw success
    GET_DEPOSIT(10, "get deposit"), //deposit success
    GET_NFT(11, "get nft"), //mint or trade result
    GET_DEFI(12, "get defi"); // defi result
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
