package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author YCL686
 * @Date 2022/10/14
 */
@Data
public class AccountEntryVO {
    private String txHash;

    private int entryType;

    private int entryEvent;

    private int status;

    private BigDecimal entryAmount;

    private BigDecimal entryBalance;

    private String entryTypeName;

    private String entryEventName;

    private String statusName;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

}
