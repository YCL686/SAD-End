package com.example.sharablead.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author inncore
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_daily_staking_record")
public class DailyStakingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long poolId;

    private BigDecimal stakingAmount;

    /**
     * 0待结算 1已结算
     */
    private Integer status;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
