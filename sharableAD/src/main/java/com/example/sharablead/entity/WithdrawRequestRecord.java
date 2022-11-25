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
 * @since 2022-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_withdraw_request_record")
public class WithdrawRequestRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long accountId;

    private BigDecimal withdrawAmount;

    private String withdrawAddress;

    /**
     * 0pending1success2fail
     */
    private Integer status;

    private String memo;

    private Long entryId;

    private LocalDate withdrawDate;

    private String txHash;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
