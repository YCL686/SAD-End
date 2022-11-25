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
@TableName("t_synchronize")
public class Synchronize implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private LocalDate synchronizeDate;

    private BigDecimal synchronizeAmount;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
