package com.example.sharablead.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author inncore
 * @since 2022-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_synchronize_record")
public class SynchronizeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long synchronizeId;

    private LocalDate synchronizeDate;

    private Integer synchronizeType;

    private BigDecimal synchronizeAmount;

    private BigDecimal totalRatio;

    private BigDecimal totalAmount;

    private BigDecimal feedBackRatio;

    private BigDecimal feedBackAmount;

    private BigDecimal onSaleRatio;

    private BigDecimal onSaleAmount;

    private BigDecimal burnRatio;

    private BigDecimal burnAmount;

    private String feedBackTxHash;

    private String onSaleTxHash;

    private String burnTxHash;

    private Integer feedBackStatus;

    private Integer burnStatus;

    private Integer onSaleStatus;

    private String feedBackMemo;

    private String burnMemo;

    private String onSaleMemo;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
