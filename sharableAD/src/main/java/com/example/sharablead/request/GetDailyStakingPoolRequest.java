package com.example.sharablead.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetDailyStakingPoolRequest {

    private Long opusId;

    private LocalDate stakingDate;
}
