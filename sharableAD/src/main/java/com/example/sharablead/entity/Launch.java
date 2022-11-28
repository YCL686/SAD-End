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
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_launch")
public class Launch implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer launchIndex;

    private LocalDateTime launchMoment;

    private LocalDate launchDate;

    private BigDecimal launchPrice;

    private BigDecimal declineRatio;

    private Integer launchCount;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
