package com.example.sharablead.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChangeBalanceEntryRequest {
    private Long entryId;
    //变更事件 参考AccountEntryEventEnum
    private Integer entryEvent;
    //变更类型 参考AccountEntryTypeEnum
    private Integer entryType;
    //变更数量
    private BigDecimal amount;
    //交易hash 如果涉及链上操作
    private String txHash;

    //交易状态
    private Integer status;

    private Long userId;
}
