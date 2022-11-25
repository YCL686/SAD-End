package com.example.sharablead.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_daily_task_config")
public class DailyTaskConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务次数
     */
    private Integer taskCount;

    /**
     * 任务关键词
     */
    private String taskKey;

    /**
     * 任务系数
     */
    private BigDecimal taskRatio;

    /**
     * 任务奖励数量
     */
    private BigDecimal taskReward;

    /**
     * 0上线 1下线
     */
    private Integer taskStatus;

    /**
     * 0每日1长期
     */
    private Integer taskType;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
