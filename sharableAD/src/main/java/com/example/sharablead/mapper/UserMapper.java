package com.example.sharablead.mapper;

import com.example.sharablead.entity.User;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author inncore
 * @since 2022-09-28
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
