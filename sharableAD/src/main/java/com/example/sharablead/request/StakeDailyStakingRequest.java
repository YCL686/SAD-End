package com.example.sharablead.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StakeDailyStakingRequest {

    private Long userId;

    private Long opusId;

    private BigDecimal stakingAmount;
}
