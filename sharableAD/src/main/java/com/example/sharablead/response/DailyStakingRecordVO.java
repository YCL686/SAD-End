package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DailyStakingRecordVO {

    private String nickName;

    private String avatarUrl;

    private Long userId;

    private BigDecimal stakingAmount;

    private Integer status;

    private String statusName;

    private LocalDateTime stakingTime;
}
