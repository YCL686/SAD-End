package com.example.sharablead.request;

import lombok.Data;

import java.util.List;

@Data
public class GenerateTokenRequest {

    private Long userId;

    private String nickName;

    private String address;

    private List<String> roleNames;
}
