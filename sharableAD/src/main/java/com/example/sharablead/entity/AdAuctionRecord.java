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
@TableName("t_ad_auction_record")
public class AdAuctionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long adAuctionId;

    private Long userId;

    private LocalDate auctionDate;

    private BigDecimal auctionUnitPrice;

    /**
     * 拍卖天数
     */
    private Integer auctionDays;

    private BigDecimal auctionTotalPrice;

    private Integer auctionType;

    private String memo;

    /**
     * 0 拍卖中 1出价被超 2拍卖成功
     */
    private Integer status;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
