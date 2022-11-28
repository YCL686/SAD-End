package com.example.sharablead.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
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
 * @since 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_daily_task_record")
public class DailyTaskRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 任务日期
     */
    private LocalDate taskDate;

    private BigDecimal taskReward;

    /**
     * 任务id
     */
    private Long taskId;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
