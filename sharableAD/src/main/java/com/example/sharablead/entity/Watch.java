package com.example.sharablead.entity;

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
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_watch")
public class Watch implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long watchedId;

    private Long watchedType;
    /**
     * 用于浏览量统计需要 限定单日最多访问多少次纳入统计 如后台某处配置3次
     * 统计信息插入时 某日 某用户浏览某作品最多统计3次
     */
    private LocalDate watchDate;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;


}
