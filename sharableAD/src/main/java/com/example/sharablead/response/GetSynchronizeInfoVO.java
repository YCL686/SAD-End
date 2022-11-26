package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author YCL686
 * @Date 2022/11/26
 */
@Data
public class GetSynchronizeInfoVO {

    private BigDecimal feedBackAmountPlus = BigDecimal.ZERO;

    private BigDecimal feedBackAmountMinus = BigDecimal.ZERO;

    private BigDecimal onSaleAmount = BigDecimal.ZERO;

    private BigDecimal burnAmount = BigDecimal.ZERO;

    private BigDecimal feedBackAmountPlusGrowthRate;

    private BigDecimal feedBackAmountMinusGrowthRate;

    private BigDecimal onSaleAmountGrowthRate;

    private BigDecimal burnAmountGrowthRate;
}
