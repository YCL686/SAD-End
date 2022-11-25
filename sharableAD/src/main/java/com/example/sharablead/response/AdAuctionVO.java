package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdAuctionVO {

    private Integer adIndex;

    private String link;

    private String resourceUrl;

    private Long adId;

    private LocalDateTime auctionStartTime;

    private Long auctionStartCountDown;

    private Long auctionEndCountDown;

    private LocalDateTime auctionEndTime;

    private BigDecimal startPrice;

    private BigDecimal buyItNowPrice;

    private BigDecimal bidPrice;

    private Integer auctionStatus;

    private String auctionStatusName;

    private Long adAuctionId;

    private Long currentHolderId;

    private String currentHolderName;

    private String currentHolderAvatarUrl;

    private String currentHolderMemo;

    private Long currentBidderId;

    private String currentBidderName;

    private String currentBidderAvatarUrl;

    private String currentBidderMemo;
}
