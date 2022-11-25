package com.example.sharablead.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer role;

    private String category;

    private String type;

    private String roleName;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

    private Integer deleted;

    private Long boundId;
    private Long userId;


}
