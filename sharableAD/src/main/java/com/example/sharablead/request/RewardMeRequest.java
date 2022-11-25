package com.example.sharablead.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RewardMeRequest {

    private Long toUserId;

    private Long fromUserId;

    private BigDecimal amount;

    private String memo;
}
