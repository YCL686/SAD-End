package com.example.sharablead.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BidBuyRequest {

    private Long userId;

    private BigDecimal auctionUnitPrice;

    private Integer auctionDays;

    private Long adAuctionId;

    private Integer auctionType;

    private String memo;
}
