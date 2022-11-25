package com.example.sharablead.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Focus;
import com.example.sharablead.request.OperateFocusRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-10-20
 */
public interface FocusService extends IService<Focus> {

    GlobalResponse operateFocus(OperateFocusRequest operateFocusRequest);
}
