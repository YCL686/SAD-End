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
 * @since 2022-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_opus")
public class Opus implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String title;

    private String summary;

    private String text;

    private String content;

    private Integer minted;

    private String mintedAddress;

    private String resourceUrl;

    private String compressResourceUrl;

    private BigDecimal hotScore;

    private Integer status;
    //首次发布时间
    private LocalDateTime publishTime;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

}
