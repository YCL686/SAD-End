package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum CronJobCodeEnum {

    CALCULATE_USER_ACTIVE(0, "计算用户活跃度"),
    CALCULATE_OPUS_HOT_SCORE(1, "计算作品活跃度"),
    CALCULATE_DAIL_TASK_REWARD(2, "计算每日任务奖励"),
    SETTLE_DAILY_STAKING(3, "结算每日质押"),
    PROCESS_WITHDRAW_REQUEST(4, "处理提现请求"),
    SYNC_WALLET(5, "同步钱包"),
    AUCTION_BUSINESS(6, "拍卖业务");


    private final int code;
    private final String name;

    CronJobCodeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (CronJobCodeEnum c : CronJobCodeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
