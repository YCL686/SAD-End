package com.example.sharablead.enums;

public enum GlobalResponseEnum {

    UNKNOWN_ERROR(-1, "server is busy, please try later"),
    SUCCESS(0, "success"),
    ERROR(1, "error"),
    TOKEN_ERROR(2, "token error");

    private int code;
    private String message;

    GlobalResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}