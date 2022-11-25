package com.example.sharablead.service;

import com.example.sharablead.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-10-22
 */
public interface RoleService extends IService<Role> {

    Role generateAppNormalRole(Long userId);

    List<String> getRoleNamesByUserId(Long userId);

}
