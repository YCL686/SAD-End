package com.example.sharablead.response;

import com.example.sharablead.entity.Synchronize;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author YCL686
 * @Date 2022/11/26
 */
@Data
public class GetSynchronizeInfoVO {

    private BigDecimal synchronizeAmount = BigDecimal.ZERO;

    private BigDecimal feedBackAmount = BigDecimal.ZERO;

    private BigDecimal onSaleAmount = BigDecimal.ZERO;

    private BigDecimal burnAmount = BigDecimal.ZERO;

    private BigDecimal synchronizeAmountGrowthRate = BigDecimal.ZERO.setScale(2);

    private BigDecimal feedBackAmountGrowthRate = BigDecimal.ZERO.setScale(2);

    private BigDecimal onSaleAmountGrowthRate = BigDecimal.ZERO.setScale(2);

    private BigDecimal burnAmountGrowthRate = BigDecimal.ZERO.setScale(2);
}
