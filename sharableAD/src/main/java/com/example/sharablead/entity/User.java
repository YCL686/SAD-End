package com.example.sharablead.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
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
 * @since 2022-09-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    private String characterSign;

    private Integer gender;

    private String avatarUrl;

    private String shortenAddress;

    /**
     * 地址
     */
    private String address;

    /**
     * 0正常1BAN
     */
    private BigDecimal activeScore;

    private Integer status;

    private String role;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
