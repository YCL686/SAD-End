package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdAuctionInfoVO {

    private BigDecimal feedbackRatio;

    private BigDecimal onSaleRatio;

    private BigDecimal shareByAuctioneerRatio;

    private BigDecimal burnRatio;

    private BigDecimal minimumBidRatio;

    private Integer maximumBidDays;
}
