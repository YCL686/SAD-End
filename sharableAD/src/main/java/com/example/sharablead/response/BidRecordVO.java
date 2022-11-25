package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BidRecordVO {

    private Long userId;

    private String nickName;

    private BigDecimal auctionUnitPrice;

    private Integer auctionDays;

    private BigDecimal auctionTotalPrice;

    private String statusName;

    private Integer status;

    private LocalDateTime gmtCreated;

    private String memo;

    private LocalDate auctionDate;

    private Integer auctionType;

    private String auctionTypeName;
}
