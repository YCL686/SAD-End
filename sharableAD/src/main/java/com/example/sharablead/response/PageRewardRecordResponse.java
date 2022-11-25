package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PageRewardRecordResponse {

    //收到的打赏总金额
    private BigDecimal totalRewardAmount = BigDecimal.ZERO;

    private BigDecimal offChainToken = BigDecimal.ZERO;

    private Long pageSize;

    private Long pageNo;

    private Long totalSize;

    private Long totalPages;

    private Boolean rewardable = true;

    private List<PageRewardRecordVO> data = new ArrayList<>();
}
