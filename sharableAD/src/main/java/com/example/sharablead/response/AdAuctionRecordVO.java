package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdAuctionRecordVO {

    private Long userId;

    private String nickName;

    private String shortenAddress;

    private BigDecimal bidPrice;

    private LocalDateTime gmtCreated;
}
