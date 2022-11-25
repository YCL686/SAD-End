package com.example.sharablead.entity;

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
 * @since 2022-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_cron_job")
public class CronJob implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 定时任务名称
     */
    private String cronJobName;

    /**
     * 定时任务描述
     */
    private String cronJobDescription;

    /**
     * 定时任务code
     */
    private Integer cronJobCode;

    private String cron;

    /**
     * 0上线 1下线
     */
    private Integer status;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

    private String memo;


}
