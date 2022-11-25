package com.example.sharablead.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawRequest {

    private BigDecimal amount;

    private String address;

    private String signature;

    private String message;

    private Long userId;

}
