package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum MessageTemplateTypeCodeEnum {
    //content
    GET_LIKE_OPUS(0, "get like opus"), //Bingo, {name} liked your opus {content}.
    GET_LIKE_COMMENT(1, "get like comment"), //Bingo, {name} liked your comment {content}.
    GET_COMMENT_OPUS(2, "get comment opus"), // {name} commented your opus {content}.
    GET_COMMENT_REPLY(3, "get comment reply"), // {name} replied your comment.
    GET_FOCUS(4,"get focus"), //Congratulations, {name} focused you.
    GET_COLLECT(5, "get collect"), //Bingo, {name} collected your opus {title}.
    GET_REWARD(6, "get reward"), //Congratulations, {name} rewarded you {amount} $SAD.
    GET_PRIVATE_MESSAGE(7, "get private message"), // {name} sent you private message: {content}.
    GET_PRIVATE_MESSAGE_REPLY(8, "get private message reply"), // {name} replied private message: {content}.
    GET_REPORT_OPUS(9, "get report opus"),
    GET_REPORT_COMMENT(10, "get report comment"),
    //activity
    GET_DAILY_TASK(21,"get dailyTask"), //Bingo, you finished {taskName} and got {amount} $SAD.
    GET_DAILY_STAKING_WIN(22, "get dailyStaking win"), // Congratulations, you won in the {date} daily staking and got {amount} $SAD.
    GET_DAILY_STAKING_LOSE(23, "get dailyStaking lose"), // Oops, you lost in the {date} daily staking.
    GET_AUCTION_SUCCESS(24,"get auction success"), //Congratulations, you succeed in the auction of {date} {index} AD.
    GET_AUCTION_EXCEED(25, "get auction exceed"), //Oops, your bid price of the {date} {index} AD auction was exceeded.
    GET_LAUNCH(26,"get launch"), //Bingo, you succeed in the {moment} of launch.
    //crypto
    GET_WITHDRAW(41, "get withdraw"), //Bingo, you withdraw {amount} $SAD successfully.
    GET_DEPOSIT(42, "get deposit"), //Congratulations, you deposited {amount} $SAD successfully.
    GET_NFT(43, "get nft"), //mint or trade result
    GET_DEFI(44, "get defi"), // defi result
    //system
    GET_SYSTEM(61, "get system"); //Attention Please, {content}.
    //TODO ...


    private final int code;
    private final String name;

    MessageTemplateTypeCodeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (MessageTemplateTypeCodeEnum c : MessageTemplateTypeCodeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
