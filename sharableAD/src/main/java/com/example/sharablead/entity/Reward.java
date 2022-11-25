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
 * @since 2022-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_reward")
public class Reward implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long fromUserId;

    private Long toUserId;

    private BigDecimal amount;

    private String memo;

    private LocalDate rewardDate;

    /**
     * 0未同步 1已同步
     */
    private Integer synchronizeFlag;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
