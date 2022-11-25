package com.example.sharablead.response;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {

    private String nickName;

    private Long userId;

    private String address;

    private String token;

    private String avatarUrl;

    private List<String> roleNames;
}
