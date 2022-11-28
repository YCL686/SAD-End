package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.LaunchRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.request.AddLaunchRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-11-28
 */
public interface LaunchRecordService extends IService<LaunchRecord> {

    GlobalResponse addLaunch(AddLaunchRequest addLaunchRequest);
}
