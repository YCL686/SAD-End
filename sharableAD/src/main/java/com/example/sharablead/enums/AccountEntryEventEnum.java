package com.example.sharablead.enums;

import lombok.Getter;

/**
 * @Author YCL686
 * @Date 2022/10/14
 */
@Getter
public enum  AccountEntryEventEnum {
    DEPOSIT(0, "Deposit"),
    WITHDRAW(1, "Withdraw"),
    DAILY_TASK(2,"DailyTask"),

    DAILY_STAKING(3, "DailyStaking"),
    REWARD(4, "REWARD"),

    AD_AUCTION(5, "ADAuction");
    //TODO more events to be added

    private final int code;
    private final String name;

    AccountEntryEventEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(int code) {
        for (AccountEntryEventEnum c : AccountEntryEventEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }

}
