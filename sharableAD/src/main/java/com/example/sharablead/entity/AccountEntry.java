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
 * @since 2022-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_account_entry")
public class AccountEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long accountId;

    /**
     * 流水类型 0收入1支出
     */
    private Integer entryType;

    /**
     * 流水事件
     */
    private Integer entryEvent;

    /**
     * 流水金额
     */
    private BigDecimal entryAmount;

    /**
     * 流水后余额
     */
    private BigDecimal entryBalance;

    private String txHash;

    /**
     * 0成功1失败2处理中
     */
    private Integer status;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
