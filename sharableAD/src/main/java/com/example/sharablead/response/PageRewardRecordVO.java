package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PageRewardRecordVO {

    private LocalDateTime gmtCreated;

    private Long toUserId;

    private Long fromUserId;

    private BigDecimal amount;

    private String toUserName;

    private String fromUserName;
}
