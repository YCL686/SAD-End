package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.request.LoginRequest;
import com.example.sharablead.response.LoginResponse;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-09-28
 */
public interface UserService extends IService<User> {


    GlobalResponse login(LoginRequest loginRequest);

    GlobalResponse logout(String token);

    Map<Long, User> getUserMap(List<Long> ids);

    GlobalResponse getProFile(boolean self, Long userId);

    GlobalResponse getMentionedList(String key);
}
