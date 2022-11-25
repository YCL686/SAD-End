package com.example.sharablead.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author YCL686
 * @Date 2022/10/14
 */
@Data
public class DepositRequest {

    private String address;

    private BigDecimal amount;

    private String hash;

    private String signature;

    private String message;

    private Long userId;
}
