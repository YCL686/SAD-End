package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sharablead.constant.AppConstant;
import com.example.sharablead.entity.Role;
import com.example.sharablead.mapper.RoleMapper;
import com.example.sharablead.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.util.IDUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-10-22
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public Role generateAppNormalRole(Long userId) {
        return generateRole("app", "member", AppConstant.APP_ID, userId, AppConstant.APP_NORMAL_MEMBER_ROLE_NAME, 0);
    }

    public Role generateRole(String category, String type, Long boundId, Long userId, String roleName, int roleValue) {
        Role role = new Role();
        role.setCategory(category);
        role.setBoundId(boundId);
        role.setDeleted(0);
        role.setId(IDUtil.nextId());
        role.setType(type);
        role.setRoleName(roleName);
        role.setUserId(userId);
        return role;
    }

    public List<String> getRoleNamesByUserId(Long userId) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getUserId, userId);
        return list(lambdaQueryWrapper).stream().map(r->r.getRoleName()).collect(Collectors.toList());
    }
}
