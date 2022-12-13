package com.example.sharablead.enums;

import lombok.Getter;

@Getter
public enum MessageTemplateCategoryCodeEnum {
    INTERACT(1, "interact"),
    ACTIVITY(2, "activity"),
    CRYPTO(3, "crypto"),
    SYSTEM(4, "system");


    private final int code;
    private final String name;

    MessageTemplateCategoryCodeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    // 普通方法
    public static String getName(int code) {
        for (MessageTemplateCategoryCodeEnum c : MessageTemplateCategoryCodeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return "-";
    }
}
