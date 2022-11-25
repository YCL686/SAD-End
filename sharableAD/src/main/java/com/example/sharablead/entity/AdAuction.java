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
 * @since 2022-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_ad_auction")
public class AdAuction implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long adId;

    private BigDecimal bidPrice;

    /**
     * 成交价
     */
    private BigDecimal dealPrice;

    private Integer dealDays;

    private BigDecimal totalPrice;

    /**
     * 0未开始 1进行中 2拍卖成功结束 3流拍
     */
    private Integer status;

    private LocalDate auctionDate;

    private LocalDateTime auctionEndTime;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
