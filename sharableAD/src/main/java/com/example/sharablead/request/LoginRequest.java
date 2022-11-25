package com.example.sharablead.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String shortenAddress;

    private String address;

    private String signature;

    private String message;
}
