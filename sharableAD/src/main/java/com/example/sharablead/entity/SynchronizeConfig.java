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
 * @since 2022-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_synchronize_config")
public class SynchronizeConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 同步类型
     */
    private Integer synchronizeType;

    private BigDecimal totalRatio;

    private BigDecimal feedBackRatio;

    private BigDecimal onSaleRatio;

    private BigDecimal burnRatio;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
