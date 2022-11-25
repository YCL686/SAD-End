package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum DailyStakingPoolStatusEnum {
    /**
     * 0可质押 1不可质押 2上榜 3未上榜
     */
    STAKABLE(0, "available to stake"),
    UNSTAKABLE(1, "unavailable to stake"),
    WIN(2, "win"),
    LOSE(3, "lose");

    private final int code;
    private final String name;

    DailyStakingPoolStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (DailyStakingPoolStatusEnum c : DailyStakingPoolStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
