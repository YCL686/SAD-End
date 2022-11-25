package com.example.sharablead.entity;

import lombok.Data;

@Data
public class Token {
    private String nickName;

    private Long userId;

    private String address;

    private String token;
}
