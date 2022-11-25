package com.example.sharablead.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Focus;
import com.example.sharablead.entity.User;
import com.example.sharablead.enums.FocusStatusEnum;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.mapper.FocusMapper;
import com.example.sharablead.request.OperateFocusRequest;
import com.example.sharablead.service.FocusService;
import com.example.sharablead.service.UserService;
import com.example.sharablead.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-10-20
 */
@Service
public class FocusServiceImpl extends ServiceImpl<FocusMapper, Focus> implements FocusService {

    @Autowired
    private FocusMapper focusMapper;

    @Autowired
    private UserService userService;

    @Override
    public GlobalResponse operateFocus(OperateFocusRequest operateFocusRequest) {

        Long userId = operateFocusRequest.getUserId();
        Long focusedId = operateFocusRequest.getFocusedId();

        User user = userService.getById(userId);

        if (Objects.isNull(user)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid user");
        }

        LambdaQueryWrapper<Focus> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Focus::getUserId, userId);
        lambdaQueryWrapper.eq(Focus::getFocusedId, focusedId);
        Focus focus = focusMapper.selectOne(lambdaQueryWrapper);
        //加关
        if (Objects.isNull(focus)){
            focus = new Focus();
            focus.setId(IDUtil.nextId());
            focus.setStatus(FocusStatusEnum.NORMAL.getCode());
            focus.setUserId(userId);
            focus.setFocusedId(focusedId);
            focus.setGmtCreated(LocalDateTime.now());
            focus.setGmtModified(LocalDateTime.now());

            if (focusMapper.insert(focus) > 0){
                return GlobalResponse.success(true);
            }
        }else {
            //取关
            if (FocusStatusEnum.NORMAL.getCode() == focus.getStatus()){
                focus.setStatus(FocusStatusEnum.CANCEL.getCode());
            }else {
                //加关
                if (FocusStatusEnum.CANCEL.getCode() == focus.getStatus()){
                    focus.setStatus(FocusStatusEnum.NORMAL.getCode());
                }
            }

            focus.setGmtModified(LocalDateTime.now());
            if (focusMapper.updateById(focus) > 0){
                return GlobalResponse.success(true);
            }
        }
        return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "focus error");
    }
}
