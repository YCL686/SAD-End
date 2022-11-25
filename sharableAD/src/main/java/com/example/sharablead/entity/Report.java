package com.example.sharablead.entity;

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
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_report")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long reportedId;

    /**
     * 被举报对象类型 0opus 1comment 2user
     */
    private Integer reportedType;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
