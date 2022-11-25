package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum CommentStatusEnum {

    NORMAL(0, "normal"),
    DELETED(1, "deleted");

    private final int code;
    private final String name;

    CommentStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (CommentStatusEnum c : CommentStatusEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }

}
