package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum MessageStatusEnum {

    UN_READ(0, "unread"),
    READ(1, "read");
    private final int code;
    private final String name;

    MessageStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (MessageStatusEnum c : MessageStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
