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
@TableName("t_ad")
public class Ad implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 序号
     */
    private Integer adIndex;

    /**
     * 名称
     */
    private String adName;

    /**
     * 悬浮文字
     */
    private String label;

    /**
     * 背景资源url
     */
    private String resourceUrl;

    /**
     * 点击跳转链接
     */
    private String link;

    /**
     * 所属用户id 0或null表示admin
     */
    private Long userId;

    private Integer editCount;

    /**
     * 状态 0正常 1审核中 2下架
     */
    private Integer status;

    private BigDecimal startPrice;

    private BigDecimal buyItNowPrice;

    private LocalDateTime nextAuctionTime;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
