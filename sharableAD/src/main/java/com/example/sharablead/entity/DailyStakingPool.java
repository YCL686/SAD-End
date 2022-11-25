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
 * @since 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_daily_staking_pool")
public class DailyStakingPool implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long opusId;

    private LocalDate stakingDate;

    private BigDecimal stakingAmount;

    /**
     * 0可质押 1不可质押 2上榜 3未上榜
     */
    private Integer status;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
