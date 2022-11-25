package com.example.sharablead.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 铭帅
 * @since 2022-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_system_config")
public class SystemConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String value;

    public SystemConfig(String hashKey, String value) {
        this.name = hashKey;
        this.value = value;
    }
}
