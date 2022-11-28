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
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_launch_record")
public class LaunchRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long launchId;

    private Long userId;

    private BigDecimal launchPrice;

    private String launchTitle;

    private String launchDescription;

    private String launchLink;

    private String launchUrl;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
